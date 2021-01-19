package com.hydbest.baseandroid.activity.jetpack.room;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface StudentDao {

    @Insert
    long insertOne(Student stu);

    @Insert
    void insertAll(Student... stu);

    @Delete
    void deleteOne(Student stu);

    @Delete
    void deleteAll(Student... stu);

    @Query("DELETE FROM Student")
    void deleteAll();

    @Update
    void updateOne(Student stu);

    @Update
    void updateAll(Student... stu);

    @Query("SELECT * FROM Student WHERE id IN (:ids) ORDER BY id ASC")
    List<Student> queryAllByIds(int[] ids);

    @Query("SELECT * FROM Student WHERE id == (:id)")
    LiveData<Student> findById(int id);

    /** %name% **/
    @Query("SELECT * FROM Student WHERE name LIKE :name LIMIT 1")
    Student findByName(String name);

    /**
     * 允许在主线程
     * @return
     */
    @Query("SELECT * FROM Student ORDER BY id ASC")
    LiveData<List<Student>> queryAll();

    @Query("SELECT * FROM Student ORDER BY id ASC")
    DataSource.Factory<Integer,Student> queryAllData();

    @Query("SELECT * FROM Student ORDER BY id ASC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    List<Student> findOnePage(int page,int pageSize);

}
