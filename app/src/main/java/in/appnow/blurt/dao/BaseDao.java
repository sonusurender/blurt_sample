package in.appnow.blurt.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface BaseDao<T> {
    /**
     * Insert an object in the database.
     *
     * @param obj the object to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(T obj);

    /**
     * Insert an array of objects in the database.
     *
     * @param list the objects to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(List<T> list);

    /**
     * Update an object from the database.
     *
     * @param obj the object to be updated
     */
    @Update
    int update(T obj);

    /**
     * Delete an object from the database
     *
     * @param obj the object to be deleted
     */
    @Delete
    int delete(T obj);

    /**
     * Delete an object from the database
     *
     * @param list the object to be deleted
     */
    @Delete
    int delete(List<T> list);
}
