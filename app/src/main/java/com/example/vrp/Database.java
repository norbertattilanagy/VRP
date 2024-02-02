package com.example.vrp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {

    public Database(Context context) {
        super(context, "vrp.db", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableNote = "CREATE TABLE problem (id INTEGER PRIMARY KEY AUTOINCREMENT, capacity INTEGER, name Text)";
        db.execSQL(createTableNote);
        String createTableOption = "CREATE TABLE location (id INTEGER PRIMARY KEY AUTOINCREMENT, problem_id INTEGER, name Text, x_coordinate FLOAT, y_coordinate FLOAT,request INTEGER, warehouse Boolean)";
        db.execSQL(createTableOption);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public int addProblem(ProblemData problemData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("capacity", problemData.getCapacity());
        contentValues.put("name", problemData.getName());

        long result = db.insert("problem", null, contentValues);
        return (int) result;
    }

    public List<ProblemData> selectAllProblem() {
        List<ProblemData> problemData = new ArrayList<>();
        String select = "SELECT * FROM problem";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(select, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                int capacity = cursor.getInt(1);
                String name = cursor.getString(2);
                problemData.add(new ProblemData(id, capacity, name));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return problemData;
    }

    public void updateProblem(String id, String capacity, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("capacity", capacity);
        values.put("name", name);

        db.update("problem", values, "id=?", new String[]{id});
        db.close();
    }

    public void deleteProblem(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("problem", "id=?", new String[]{id});
        db.close();
    }

    public int addLocation(VRPLocation location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("problem_id", location.getProblem_id());
        contentValues.put("name", location.getName());
        contentValues.put("x_coordinate", location.getX());
        contentValues.put("y_coordinate", location.getY());
        contentValues.put("request", location.getRequest());
        contentValues.put("warehouse", location.getWarehouse());

        long result = db.insert("location", null, contentValues);
        return (int) result;
    }

    public List<VRPLocation> selectIdLocation(int problemId) {
        List<VRPLocation> locations = new ArrayList<>();
        String select = "SELECT * FROM location WHERE problem_id = " + problemId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(select, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                int probId = cursor.getInt(1);
                String name = cursor.getString(2);
                int x_coordinate = cursor.getInt(3);
                int y_coordinate = cursor.getInt(4);
                int request = cursor.getInt(5);
                Boolean warehouse = false;
                if (cursor.getString(6).equals("1"))
                    warehouse = true;

                locations.add(new VRPLocation(id, problemId, name, x_coordinate, y_coordinate, request, warehouse));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return locations;
    }

    public void updateLocation(List<VRPLocation> updLocation) {
        List<VRPLocation> locations = selectIdLocation(updLocation.get(0).getProblem_id());

        //delete inexistent location
        for (int i = 0; i < locations.size(); i++) {
            Boolean delete = true;
            for (int j = 0; j < updLocation.size(); j++) {
                if (updLocation.get(j).getId() == locations.get(i).getId()) {
                    delete = false;
                    break;
                }
            }
            if (delete) {
                deleteLocation(locations.get(i));
            }
        }

        //insert inexistent location
        for (int i = 0; i < updLocation.size(); i++) {
            int locationId = -1;
            for (int j = 0; j < locations.size(); j++) {
                if (updLocation.get(i).getId() == locations.get(j).getId()) {
                    locationId = locations.get(i).getId();
                    break;
                }
            }
            if (locationId == -1) {
                addLocation(updLocation.get(i));
            } else {
                //x_coordinate FLOAT, y_coordinate FLOAT,request INTEGER
                SQLiteDatabase db = this.getWritableDatabase();
                String name = updLocation.get(i).getName();
                double x_coordinate = updLocation.get(i).getX();
                double y_coordinate = updLocation.get(i).getY();
                int request = updLocation.get(i).getRequest();

                String updateOption = "UPDATE location SET name = '" + name + "'" +
                        ",x_coordinate = " + x_coordinate +
                        ",y_coordinate = " + y_coordinate +
                        ",request = " + request +
                        " WHERE id LIKE " + locationId;
                db.execSQL(updateOption);
            }
        }
    }

    public void deleteLocation(VRPLocation location){
        SQLiteDatabase db = this.getWritableDatabase();
        String deleteOption = "DELETE FROM location WHERE id LIKE " + location.getId();
        db.execSQL(deleteOption);
    }

    public void deleteLocationProblemId(int problem_id){
        SQLiteDatabase db = this.getWritableDatabase();
        String deleteOption = "DELETE FROM location WHERE problem_id LIKE " + problem_id;
        db.execSQL(deleteOption);
    }
}
