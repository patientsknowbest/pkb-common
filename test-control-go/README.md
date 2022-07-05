# test-control-client-go

Test control server implementation for go applications developed by PKB.

Application must provide an implementation of testcontrol.TestControl and call RunTestControl in a goroutine in order to 
participate in test-control.

java equivalent lives in [test-control](../test-control) folder, with message structures defined in [here](../test-control-client/src/main/java/com/pkb/common/testcontrol/message)

Example:

```go
package main

import (
        "context"
	"github.com/patientsknowbest/pkb-common/test-control"
	"log"
)

type myTestControl struct{}

/// Implement TestControl interface for myTestControl
/// func SetNamespace...
///

func main() {
	go log.Fatal(testcontrol.RunTestControl(context.Background(), ":9876", "http://test-control:9876", "foo", "http://foo:9876", myTestControl{}))
}
```
