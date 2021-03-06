package tkom.ast.statement;

import lombok.Getter;
import tkom.ast.Value;

@Getter
public class ExecuteOut {
    private final Value returnedValue;
    private final ExecuteStatus status;

    public ExecuteOut(ExecuteStatus status, Value returnedValue) {
        this.returnedValue = returnedValue;
        this.status = status;
    }

    public ExecuteOut(ExecuteStatus status) {
        this.returnedValue = null;
        this.status = status;
    }

    public boolean isReturnCall() {
        return status.equals(ExecuteStatus.RETURN);
    }


    public enum ExecuteStatus {
        NORMAL,
        RETURN
    }
}
