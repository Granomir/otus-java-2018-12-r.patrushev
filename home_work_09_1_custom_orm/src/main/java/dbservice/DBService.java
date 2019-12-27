package dbservice;

public interface DBService<T> {
    long create(T objectData);

    void update(T objectData);

    long createOrUpdate(T objectData);

    T load(long id);
}
