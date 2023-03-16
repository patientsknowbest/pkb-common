package testcontrol

import (
	"context"
	"fmt"
	"net/http"
	"strings"
	"testing"
	"time"
)

type testTestControl struct {
	didSetNamespace     bool
	namespace           string
	errorOnSetNamespace error
}

func (t *testTestControl) SetNamespace(ctx context.Context, newNamespace string) error {
	t.didSetNamespace = true
	t.namespace = newNamespace
	return t.errorOnSetNamespace
}
func (t *testTestControl) InjectConfig(ctx context.Context, key string, value string) error {
	return nil
}
func (t *testTestControl) SetFixedTimestamp(ctx context.Context, timestamp string) error {
	return nil
}
func (t *testTestControl) MoveTime(ctx context.Context, amount int, unit string) error {
	return nil
}
func (t *testTestControl) ClearInternalState(ctx context.Context, clearFixedTimestamp bool) error {
	return nil
}
func (t *testTestControl) ClearStorage(ctx context.Context) error {
	return nil
}
func (t *testTestControl) LogTestName(ctx context.Context, testName string) error {
	return nil
}
func (t *testTestControl) ToggleDetailedLogging(ctx context.Context, enable bool) error {
	return nil
}
func (t *testTestControl) SuspendProcessing(ctx context.Context) error {
	return nil
}
func (t *testTestControl) ResumeProcessing(ctx context.Context) error {
	return nil
}

func TestSetNamespace(t *testing.T) {
	withTestControlServer(t, func(t *testing.T, testControlBaseUrl string, impl *testTestControl) {
		request, err := http.NewRequest(http.MethodPut, testControlBaseUrl+"/io-pkb-testcontrol-setNamespace", strings.NewReader(`{"newNamespace": "fooNamespace"}`))
		if err != nil {
			t.Fatal(err)
		}
		response, err := http.DefaultClient.Do(request)
		if err != nil {
			t.Fatal(err)
		}
		defer response.Body.Close()
		if response.StatusCode != http.StatusNoContent {
			t.Fatalf("unexpected status %s", response.Status)
		}

		if !impl.didSetNamespace {
			t.Errorf("expected didSetNamespace=true")
		}
		if impl.namespace != "fooNamespace" {
			t.Errorf("expected namespace=fooNamespace")
		}
	})
}

func TestSetNamespaceError(t *testing.T) {
	withTestControlServer(t, func(t *testing.T, testControlBaseUrl string, impl *testTestControl) {
		impl.errorOnSetNamespace = fmt.Errorf("oh noes an error")
		request, err := http.NewRequest(http.MethodPut, testControlBaseUrl+"/io-pkb-testcontrol-setNamespace", strings.NewReader(`{"newNamespace": "fooNamespace"}`))
		if err != nil {
			t.Fatal(err)
		}
		response, err := http.DefaultClient.Do(request)
		if err != nil {
			t.Fatal(err)
		}
		if response.StatusCode != http.StatusInternalServerError {
			t.Errorf("unexpected status code %s", response.Status)
		}
	})
}

func withTestControlServer(t *testing.T, f func(*testing.T, string, *testTestControl)) {
	// boot the test control server
	serverContext, cancel := context.WithCancel(context.Background())
	defer cancel()
	testControlListenAddress := "127.0.0.1:9876"
	testControlBaseUrl := "http://" + testControlListenAddress
	impl := &testTestControl{}
	go RunTestControl(serverContext, testControlListenAddress, "", "foo", "foo", impl)

	// Wait for it to be healthy
	healthUrl := testControlBaseUrl + "/health"
	timeout := 2 * time.Second
	cx2, cancel := context.WithTimeout(context.Background(), timeout)
	defer cancel()
	for {
		request, err := http.NewRequestWithContext(cx2, http.MethodGet, healthUrl, nil)
		if err != nil {
			t.Fatal(err)
		}
		res, err := http.DefaultClient.Do(request)
		if err == nil && res.StatusCode == http.StatusOK {
			break
		}
		select {
		case <-cx2.Done():
			t.Fatalf("failed to reach %s after %v", testControlBaseUrl, timeout)
		case <-time.After(50 * time.Millisecond):
			continue
		}
	}

	// Run the test
	f(t, testControlBaseUrl, impl)
}
