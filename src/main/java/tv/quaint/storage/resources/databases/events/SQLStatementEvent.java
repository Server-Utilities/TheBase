package tv.quaint.storage.resources.databases.events;

public class SQLStatementEvent extends DBStatementEvent {
    public SQLStatementEvent(String statement) {
        super(statement);
    }
}
