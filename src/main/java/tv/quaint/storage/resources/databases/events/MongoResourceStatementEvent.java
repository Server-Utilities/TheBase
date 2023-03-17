package tv.quaint.storage.resources.databases.events;

import lombok.Getter;
import lombok.Setter;
import tv.quaint.storage.resources.databases.specific.MongoResource;

public class MongoResourceStatementEvent extends MongoStatementEvent {
    @Getter
    @Setter
    private MongoResource resource;

    public MongoResourceStatementEvent(MongoResource resource, String statement) {
        super(statement);
        this.resource = resource;
    }
}
