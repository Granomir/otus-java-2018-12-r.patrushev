package dbservice;

public interface JDBCTemplate {

    <T> long create(T objectData);

    <T> void update(T objectData);

    <T> long createOrUpdate(T objectData);

    <T> T load(long id, Class<T> clazz);

}
