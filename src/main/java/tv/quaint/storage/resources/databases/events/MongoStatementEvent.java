package tv.quaint.storage.resources.databases.events;

public class MongoStatementEvent extends DBStatementEvent {
    public MongoStatementEvent(String statement) {
        super(statement);
    }

    public static String createDatabase(String databaseName) {
        return "use " + databaseName;
    }

    public static String createCollection(String collectionName) {
        return "db.createCollection(\"" + collectionName + "\")";
    }

    public static String dropCollection(String collectionName) {
        return "db." + collectionName + ".drop()";
    }

    public static String dropDatabase(String databaseName) {
        return "db.dropDatabase()";
    }

    public static String insert(String collectionName, String json) {
        return "db." + collectionName + ".insert(" + json + ")";
    }

    public static String find(String collectionName, String json) {
        return "db." + collectionName + ".find(" + json + ")";
    }

    public static String findOne(String collectionName, String json) {
        return "db." + collectionName + ".findOne(" + json + ")";
    }

    public static String update(String collectionName, String json) {
        return "db." + collectionName + ".update(" + json + ")";
    }

    public static String remove(String collectionName, String json) {
        return "db." + collectionName + ".remove(" + json + ")";
    }

    public static String count(String collectionName, String json) {
        return "db." + collectionName + ".count(" + json + ")";
    }

    public static String distinct(String collectionName, String json) {
        return "db." + collectionName + ".distinct(" + json + ")";
    }

    public static String aggregate(String collectionName, String json) {
        return "db." + collectionName + ".aggregate(" + json + ")";
    }

public static String mapReduce(String collectionName, String json) {
        return "db." + collectionName + ".mapReduce(" + json + ")";
    }

    public static String group(String collectionName, String json) {
        return "db." + collectionName + ".group(" + json + ")";
    }

    public static String createIndex(String collectionName, String json) {
        return "db." + collectionName + ".createIndex(" + json + ")";
    }

    public static String ensureIndex(String collectionName, String json) {
        return "db." + collectionName + ".ensureIndex(" + json + ")";
    }

    public static String dropIndex(String collectionName, String json) {
        return "db." + collectionName + ".dropIndex(" + json + ")";
    }

    public static String reIndex(String collectionName, String json) {
        return "db." + collectionName + ".reIndex(" + json + ")";
    }

    public static String getIndexes(String collectionName, String json) {
        return "db." + collectionName + ".getIndexes(" + json + ")";
    }

    public static String getCollectionNames(String collectionName, String json) {
        return "db." + collectionName + ".getCollectionNames(" + json + ")";
    }

    public static String getCollection(String collectionName, String json) {
        return "db." + collectionName + ".getCollection(" + json + ")";
    }

    public static String getLastError(String collectionName, String json) {
        return "db." + collectionName + ".getLastError(" + json + ")";
    }

    public static String getLastErrorObj(String collectionName, String json) {
        return "db." + collectionName + ".getLastErrorObj(" + json + ")";
    }

    public static String resetError(String collectionName, String json) {
        return "db." + collectionName + ".resetError(" + json + ")";
    }

    public static String runCommand(String collectionName, String json) {
        return "db." + collectionName + ".runCommand(" + json + ")";
    }

    public static String forceError(String collectionName, String json) {
        return "db." + collectionName + ".forceError(" + json + ")";
    }

    public static String getPrevError(String collectionName, String json) {
        return "db." + collectionName + ".getPrevError(" + json + ")";
    }

    public static String getMongo(String collectionName, String json) {
        return "db." + collectionName + ".getMongo(" + json + ")";
    }

    public static String getDB(String collectionName, String json) {
        return "db." + collectionName + ".getDB(" + json + ")";
    }

    public static String getCollectionInfos(String collectionName, String json) {
        return "db." + collectionName + ".getCollectionInfos(" + json + ")";
    }

    public static String getCollectionInfo(String collectionName, String json) {
        return "db." + collectionName + ".getCollectionInfo(" + json + ")";
    }

    public static String getCollectionNames(String collectionName) {
        return "db.getCollectionNames()";
    }

    public static String getCollection(String collectionName) {
        return "db.getCollection(\"" + collectionName + "\")";
    }

    public static String getLastError(String collectionName) {
        return "db.getLastError()";
    }

    public static String getLastErrorObj(String collectionName) {
        return "db.getLastErrorObj()";
    }

    public static String resetError(String collectionName) {
        return "db.resetError()";
    }

    public static String runCommand(String collectionName) {
        return "db.runCommand()";
    }

    public static String forceError(String collectionName) {
        return "db.forceError()";
    }

    public static String getPrevError(String collectionName) {
        return "db.getPrevError()";
    }

    public static String getMongo(String collectionName) {
        return "db.getMongo()";
    }

    public static String getDB(String collectionName) {
        return "db.getDB()";
    }

    public static String getCollectionInfos(String collectionName) {
        return "db.getCollectionInfos()";
    }

    public static String getCollectionInfo(String collectionName) {
        return "db.getCollectionInfo()";
    }

    public static String getCollectionNames() {
        return "db.getCollectionNames()";
    }

    public static String getCollection() {
        return "db.getCollection()";
    }

    public static String getLastError() {
        return "db.getLastError()";
    }

    public static String getLastErrorObj() {
        return "db.getLastErrorObj()";
    }

    public static String resetError() {
        return "db.resetError()";
    }

    public static String runCommand() {
        return "db.runCommand()";
    }

    public static String forceError() {
        return "db.forceError()";
    }

    public static String getPrevError() {
        return "db.getPrevError()";
    }

    public static String getMongo() {
        return "db.getMongo()";
    }

    public static String getDB() {
        return "db.getDB()";
    }

    public static String getCollectionInfos() {
        return "db.getCollectionInfos()";
    }

    public static String getCollectionInfo() {
        return "db.getCollectionInfo()";
    }
}
