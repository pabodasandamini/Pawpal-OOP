package com.pawpal.model;

/**
 * Pet.java
 * This is a Model class that represents the 'pets' table in our MySQL database.
 * OOP CONCEPT: ENCAPSULATION
 * Notice that all variables are declared as 'private'. This means they are hidden 
 * from other classes. To access them, we must use the public getter methods below.
 * This protects the data from unauthorized modification.
 */
public class Pet {

    // Private attributes (Fields) representing the columns in the 'pets' database table
    private int petId;       // Unique identifier for the pet (Primary Key)
    private int ownerId;     // Foreign Key linking to the 'users' table
    private String petName;  
    private String species;  // e.g., Dog, Cat
    private String breed;    // e.g., Labrador, Persian
    private int age;         

    /**
     * Parameterized Constructor.
     * This is called when we create a new Pet object (e.g., new Pet(...)).
     * It initializes the object with the data provided.
     * 
     * When a user registers a pet in our GUI, the UI collects the data and 
     * passes it to this constructor to create a Pet object before saving it to the database."
     */
    public Pet(int petId, int ownerId, String petName,
               String species, String breed, int age) {

        this.petId = petId;
        this.ownerId = ownerId;
        this.petName = petName;
        this.species = species;
        this.breed = breed;
        this.age = age;
    }

    // ==========================================
    // GETTER METHODS (Read Access)
    // These methods allow other classes (like PetDAO or the GUI) to safely read 
    // the private data without changing it. This is a key part of Encapsulation.
    // ==========================================

    /**
     * Returns the unique ID of the pet.
     */
    public int getPetId() {
        return petId;
    }

    /**
     * Returns the User ID of the owner who owns this pet.
     */
    public int getOwnerId() {
        return ownerId;
    }

    /**
     * Returns the name of the pet.
     */
    public String getPetName() {
        return petName;
    }

    /**
     * Returns the species of the pet (e.g., Dog).
     */
    public String getSpecies() {
        return species;
    }

    /**
     * Returns the breed of the pet (e.g., Labrador).
     */
    public String getBreed() {
        return breed;
    }

    /**
     * Returns the age of the pet.
     */
    public int getAge() {
        return age;
    }
}
