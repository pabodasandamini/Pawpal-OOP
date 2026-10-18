package com.pawpal.dao;

import com.pawpal.model.Pet;
import com.pawpal.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * "DAO stands for Data Access Object. This class is solely responsible for 
 * communicating with the MySQL database. It achieves ABSTRACTION by implementing 
 * the BaseDAO interface, which hides the complex SQL code from the rest of the app."
 */
public class PetDAO implements BaseDAO<Pet> {

    /**
     * ---------------------------------------------------------------------
     * METHOD: add(Pet p) - CREATE OPERATION
     * ---------------------------------------------------------------------
      prevent SQL Injection?
     * "I use PreparedStatement with '?' placeholders instead of string 
     * concatenation. This ensures the database treats user input as data, 
     * not as executable SQL code."
     */
    @Override
    public boolean add(Pet p) {
        // 1. Write the SQL INSERT statement with placeholders (?)
        String sql = "INSERT INTO pets (owner_id, pet_name, species, breed, age) VALUES (?, ?, ?, ?, ?)";

        // 2. Use try-with-resources to automatically close the connection when done
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // 3. Bind the Java object's data to the SQL placeholders (1 through 5)
            ps.setInt(1, p.getOwnerId());       // 1st ? = owner_id
            ps.setString(2, p.getPetName());    // 2nd ? = pet_name
            ps.setString(3, p.getSpecies());    // 3rd ? = species
            ps.setString(4, p.getBreed());      // 4th ? = breed
            ps.setInt(5, p.getAge());           // 5th ? = age

            // 4. executeUpdate() returns an int. If it returns 1, the row was inserted successfully.
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            // If the database crashes or the query fails, print the error and return false
            System.out.println("Error saving pet: " + e.getMessage());
            return false;
        }
    }

    /**
     * ---------------------------------------------------------------------
     * METHOD: findAll() - READ OPERATION (All Pets)
     * ---------------------------------------------------------------------
     *  Why is this method here?
     * "It is required by the BaseDAO interface. It retrieves every pet in the 
     * database without any filtering."
     */
    @Override
    public List<Pet> findAll() {
        // We pass '0' as the ownerId because the query has no '?' placeholder, so it's ignored.
        return query("SELECT * FROM pets", 0);
    }

    /**
     * ---------------------------------------------------------------------
     * METHOD: findByOwner(int ownerId) - READ OPERATION (Filtered)
     * ---------------------------------------------------------------------
     * How does the system know which pets belong to whom?
     * "We use a WHERE clause. This method only retrieves pets where the 
     * owner_id matches the currently logged-in user."
     */
    public List<Pet> findByOwner(int ownerId) {
        return query("SELECT * FROM pets WHERE owner_id = ?", ownerId);
    }

    /**
     * ---------------------------------------------------------------------
     * METHOD: delete(int id) - DELETE OPERATION
     * ---------------------------------------------------------------------
     *  How do you delete a specific pet?
     * "We use the primary key (pet_id) in the WHERE clause to ensure only 
     * the selected pet is deleted, not the whole table."
     */
    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM pets WHERE pet_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            
            // > 0 means at least one row was deleted. 
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting pet: " + e.getMessage());
            return false;
        }
    }

    /**
     * ---------------------------------------------------------------------
     * HELPER METHOD: query(String sql, int ownerId)
     * ---------------------------------------------------------------------
     *  Why did you use a helper method?
     * "To follow the DRY principle (Don't Repeat Yourself). Both findAll() and 
     * findByOwner() need to execute SQL and convert the results into Java objects. 
     * Instead of writing that code twice, I put it in this private method."
     * 
     * HOW IT WORKS:
     * 1. Executes the SQL query.
     * 2. Loops through the database results (ResultSet).
     * 3. Converts each row into a Pet object using the Pet constructor.
     * 4. Adds the object to a List and returns it.
     */
    private List<Pet> query(String sql, int ownerId) {
        List<Pet> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Check if the SQL string actually has a placeholder '?' to fill
            if (sql.contains("?")) {
                ps.setInt(1, ownerId);
            }

            // Execute the SELECT query. ResultSet holds the table data returned by MySQL.
            try (ResultSet rs = ps.executeQuery()) {
                
                // rs.next() moves the cursor down one row. It returns false when there are no more rows.
                while (rs.next()) {
                    
                    // Extract data from the current database row using column names
                    // and pass it into the Pet model constructor.
                    Pet pet = new Pet(
                            rs.getInt("pet_id"),
                            rs.getInt("owner_id"),
                            rs.getString("pet_name"),
                            rs.getString("species"),
                            rs.getString("breed"),
                            rs.getInt("age")
                    );

                    // Add the created Pet object to our list
                    list.add(pet);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error loading pets: " + e.getMessage());
        }

        // Return the populated list to the GUI so it can be displayed in the JTable
        return list;
    }
}
