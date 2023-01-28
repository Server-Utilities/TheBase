package tv.quaint.storage.resources.databases.events;

import lombok.Getter;
import lombok.Setter;
import tv.quaint.events.components.BaseEvent;

public class SQLStatementEvent extends BaseEvent {
    @Getter @Setter
    String statement;

    public SQLStatementEvent(String statement) {
        this.statement = statement;
    }
}
