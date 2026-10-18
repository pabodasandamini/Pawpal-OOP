
package com.pawpal.model;

// Admin class inherits common user details and methods from User
public class Admin extends User {
    
    // Constructor: initializes the admin's user details
    public Admin(int userId, String fullName, String email,
                 String password, String city) {
        
        // Calls the parent User constructor to initialize common details
        super(userId, fullName, email, password, city);
    }
    // Overrides the parent method to identify this user's role as ADMIN
    @Override
    public String getRole() {
        return "ADMIN";
    }
    // Overrides the parent method to set the admin dashboard title
    @Override
    public String getDashboardTitle() {
        return "Admin Dashboard";
    }
    // Returns an empty SQL condition, so no additional
    // appointment filter is applied by this method
    @Override
    public String getAppointmentScopeSql() {
        return "";
    }
     // Returns false because admins cannot book appointments
    // according to this class's permission rule
    @Override
    public boolean canBookAppointments() {
        return false;
    }
}
