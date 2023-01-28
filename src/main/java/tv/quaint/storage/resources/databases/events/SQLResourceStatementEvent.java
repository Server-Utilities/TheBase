package tv.quaint.storage.resources.databases.events;

import lombok.Getter;
import lombok.Setter;
import tv.quaint.events.components.BaseEvent;
import tv.quaint.storage.resources.databases.SQLResource;

public class SQLResourceStatementEvent extends SQLStatementEvent {
    @Getter @Setter
    SQLResource resource;

    public SQLResourceStatementEvent(SQLResource resource, String statement) {
        super(statement);
        this.resource = resource;
    }
}
