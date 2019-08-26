import java.util.Optional;

public interface DBService {

    <T> long create(T objectData);

    <T> void update(T objectData);

    <T> void createOrUpdate(T objectData);

    <T> T load(long id, Class<T> clazz);

}
