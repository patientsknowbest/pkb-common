package testcontrol

import (
	"bytes"
	"context"
	"encoding/json"
	"fmt"
	"log"
	"net/http"
)

// TestControl / applications should provide an implementation of this interface to participate in test-control.
type TestControl interface {
	SetNamespace(ctx context.Context, newNamespace string) error
	InjectConfig(ctx context.Context, key string, value string) error
	SetFixedTimestamp(ctx context.Context, timestamp string) error
	MoveTime(ctx context.Context, amount int, unit string) error
	ClearInternalState(ctx context.Context, clearFixedTimestamp bool) error
	ClearStorage(ctx context.Context) error
	LogTestName(ctx context.Context, testName string) error
	ToggleDetailedLogging(ctx context.Context, enable bool) error
}

// RunTestControl / applications should call this to optionally register with the test-control server
// and handle test-control requests.
// ctx: when this context is cancelled, the server is stopped.
// listenAddress: the address this application will listen on for test-control requests
// registrationEndpoint: the address of a test-control server to register with. If empty, we won't register.
// myName, myCallbackUrl: application name and callback URL for the test-control server to call this application on.
// control: implementation of TestControl interface, handling this applications state etc.
// This function blocks until the supplied context ctx is cancelled.
func RunTestControl(
	ctx context.Context,
	listenAddress, registrationEndpoint, myName, myCallbackUrl string,
	control TestControl,
) error {
	if registrationEndpoint != "" {
		err := register(registrationEndpoint, myName, myCallbackUrl)
		if err != nil {
			return err
		}
	}

	impl := testControlImpl{control}
	sm := http.NewServeMux()
	IoPkbTestcontrolPrefix := "io-pkb-testcontrol-"
	sm.HandleFunc("/"+IoPkbTestcontrolPrefix+"setNamespace", impl.handleSetNamespace)
	sm.HandleFunc("/"+IoPkbTestcontrolPrefix+"injectConfig", impl.handleInjectConfig)
	sm.HandleFunc("/"+IoPkbTestcontrolPrefix+"setFixedTimestamp", impl.handleSetFixedTimestamp)
	sm.HandleFunc("/"+IoPkbTestcontrolPrefix+"moveTime", impl.handleMoveTime)
	sm.HandleFunc("/"+IoPkbTestcontrolPrefix+"clearInternalState", impl.handleClearInternalState)

	sm.HandleFunc("/"+IoPkbTestcontrolPrefix+"clearStorage", impl.handleClearStorage)
	sm.HandleFunc("/"+IoPkbTestcontrolPrefix+"logTestName", impl.handleLogTestName)
	sm.HandleFunc("/"+IoPkbTestcontrolPrefix+"toggleDetailedLogging", impl.handleToggleDetailedLogging)
	sm.HandleFunc("/health", impl.handleHealth)
	log.Printf("starting test-control API on %s", listenAddress)
	svr := &http.Server{Addr: listenAddress, Handler: sm}
	go func() {
		<-ctx.Done()
		_ = svr.Shutdown(ctx)
	}()
	err := svr.ListenAndServe()
	if err != http.ErrServerClosed {
		return err
	}
	return nil
}

type testControlImpl struct {
	TestControl
}

func handle[T any](res http.ResponseWriter, req *http.Request, t T, f func(context.Context, T) error) {
	log.Printf("handling test control request %s", req.URL.Path)
	err := json.NewDecoder(req.Body).Decode(t)
	if err != nil {
		log.Println(err)
		res.WriteHeader(http.StatusBadRequest)
		_, _ = res.Write([]byte(err.Error()))
		return
	}
	err = f(req.Context(), t)
	if err != nil {
		log.Println(err)
		res.WriteHeader(http.StatusInternalServerError)
		_, _ = res.Write([]byte(err.Error()))
		return
	}
	res.WriteHeader(http.StatusNoContent)
	log.Printf("handling test control request %s complete", req.URL.Path)
}

func (t *testControlImpl) handleSetNamespace(res http.ResponseWriter, req *http.Request) {
	type setNamespaceReq struct {
		NewNamespace string `json:"newNamespace"`
	}
	v := &setNamespaceReq{}
	handle(res, req, v, func(ctx context.Context, v *setNamespaceReq) error {
		return t.SetNamespace(req.Context(), v.NewNamespace)
	})
}

func (t *testControlImpl) handleToggleDetailedLogging(res http.ResponseWriter, req *http.Request) {
	type request struct {
		EnableDetailedLogging bool `json:"enableDetailedLogging"`
	}
	v := &request{}
	handle(res, req, v, func(ctx context.Context, v *request) error {
		return t.ToggleDetailedLogging(ctx, v.EnableDetailedLogging)
	})
}

func (t *testControlImpl) handleInjectConfig(res http.ResponseWriter, req *http.Request) {
	type request struct {
		Key   string `json:"key"`
		Value string `json:"value"`
	}
	v := &request{}
	handle(res, req, v, func(ctx context.Context, v *request) error {
		return t.InjectConfig(ctx, v.Key, v.Value)
	})
}

func (t *testControlImpl) handleSetFixedTimestamp(res http.ResponseWriter, req *http.Request) {
	type request struct {
		Timestamp string `json:"timestamp"`
	}
	v := &request{}
	handle(res, req, v, func(ctx context.Context, v *request) error {
		return t.SetFixedTimestamp(ctx, v.Timestamp)
	})
}

func (t *testControlImpl) handleMoveTime(res http.ResponseWriter, req *http.Request) {
	type request struct {
		Amount int    `json:"amount"`
		Unit   string `json:"unit"`
	}
	v := &request{}
	handle(res, req, v, func(ctx context.Context, v *request) error {
		return t.MoveTime(ctx, v.Amount, v.Unit)
	})
}

func (t *testControlImpl) handleClearInternalState(res http.ResponseWriter, req *http.Request) {
	type request struct {
		ClearFixedTimestamp bool `json:"clearFixedTimestamp"`
	}
	v := &request{}
	handle(res, req, v, func(ctx context.Context, v *request) error {
		return t.ClearInternalState(ctx, v.ClearFixedTimestamp)
	})
}

func (t *testControlImpl) handleClearStorage(res http.ResponseWriter, req *http.Request) {
	type request struct{}
	v := &request{}
	handle(res, req, v, func(ctx context.Context, v *request) error {
		return t.ClearStorage(ctx)
	})
}

func (t *testControlImpl) handleLogTestName(res http.ResponseWriter, req *http.Request) {
	type request struct {
		TestName string ``
	}
	v := &request{}
	handle(res, req, v, func(ctx context.Context, v *request) error {
		return t.LogTestName(ctx, v.TestName)
	})
}

func (t *testControlImpl) handleHealth(res http.ResponseWriter, req *http.Request) {
	res.WriteHeader(http.StatusOK)
	res.Write([]byte(`{
  		"status": "pass",
  	`))
}

func register(registrationEndpoint, myName, myCallbackUrl string) error {
	log.Printf("registering with test-control server %s name %s callback %s", registrationEndpoint, myName, myCallbackUrl)
	startup := struct {
		Name     string `json:"name"`
		Callback string `json:"callback"`
	}{
		Name:     myName,
		Callback: myCallbackUrl,
	}
	startMsg, err := json.Marshal(startup)
	if err != nil {
		return err
	}
	req, err := http.NewRequest(http.MethodPut, registrationEndpoint, bytes.NewReader(startMsg))
	if err != nil {
		return err
	}
	resp, err := http.DefaultClient.Do(req)
	if err != nil {
		return err
	}
	if resp.StatusCode < 200 || resp.StatusCode >= 400 {
		return fmt.Errorf("unexpected status code from registration %d %s", resp.StatusCode, resp.Status)
	}
	log.Printf("registered with test-control %v, %s", startup, registrationEndpoint)
	return nil
}
