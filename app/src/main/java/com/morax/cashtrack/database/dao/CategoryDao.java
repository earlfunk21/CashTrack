package com.morax.cashtrack.database.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.morax.cashtrack.database.entity.Category;

import java.util.List;

@Dao
public interface CategoryDao {

    @Insert
    void insert(Category category);

    @Query("DELETE FROM Category")
    void deleteAll();
    @Delete
    void delete(Category category);

    @Query("SELECT * FROM Category")
    List<Category> getCategories();

    @Query("SELECT * FROM Category WHERE name = :name")
    Category getCategoryByName(String name);
}
