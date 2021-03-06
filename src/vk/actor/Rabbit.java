package vk.actor;
import java.util.List;
import java.util.Random;

import vk.model.Field;
import vk.model.Location;
import vk.simulation.Randomizer;

/**
 * A simple model of a rabbit.
 * Rabbits age, move, breed, and die.
 * 
 * @author David J. Barnes and Michael Kolling
 * @version 2008.03.30
 */
public class Rabbit extends Animal
{
    // Characteristics shared by all rabbits (static fields).

    // The age at which a rabbit can start to breed.
    private static final int BREEDING_AGE = 5;
    // The age to which a rabbit can live.
    private static final int MAX_AGE = 40;
    // The likelihood of a rabbit breeding.
    private static final double BREEDING_PROBABILITY = 0.10;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    // Random infection chance
    private static final double INFECTION_CHANCE = 1;

    // Individual characteristics (instance fields).
    // The rabbit's age.
    private int age;
    // The rabbit's sex.
    public char sex;

    /**
     * Create a new rabbit. A rabbit may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the rabbit will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Rabbit(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        age = 0;
        sex = chooseSex();
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
        }
    }
    
    /**
     * This is what the rabbit does most of the time - it runs 
     * around. Sometimes it will breed or die of old age.
     * @param newRabbits A list to add newly born rabbits to.
     */
    public void act(List<Actor> newRabbits)
    {
        incrementAge();
        if(isAlive()) {
        	infectionChance();
        }
        if(isAlive()) {
            giveBirth(newRabbits);            
            // Try to move into a free location.
            Location newLocation = getField().freeAdjacentLocation(getLocation());
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                // Overcrowding.
                setDead();
            }
        }
    }
    
    /**
     * The getter for the sex of the rabbit.
     * @return a 'm' or 'f'
     */
    public char getSex()
    {
    	return sex;
    }
    /**
     * Increase the age.
     * This could result in the rabbit's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
    
    /**
     * This method gives the rabbit a chance to get sick and turn into a zombie rabbit
     */
    private void infectionChance()
    {
    	Field field = getField();
    	double random = rand.nextInt(10001);
    	if(random==INFECTION_CHANCE) {
    		Location location = getLocation();
    		setDead();
            new ZombieRabbit(true, field, location);
    	}
	}
    
    /**
     * Check whether or not this rabbit is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newRabbits A list to add newly born rabbits to.
     */
    private void giveBirth(List<Actor> newRabbits)
    {
        // New rabbits are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        if(mate()==true) {
	        for(int b = 0; b < births && free.size() > 0; b++) {
	            Location loc = free.remove(0);
	            Rabbit young = new Rabbit(false, field, loc);
	            newRabbits.add(young);
	        }
        }
    }
    
    /**
     * Check if the rabbit is standing next to a member of the opposite sex
     * @return true/false
     */
    private boolean mate()
    {
    	Field field = getField();
    	List<Actor> animals = field.getAnimalsAdjacentLocations(getLocation());
    	for(int a = 0; a < animals.size(); a++ ) {
    		if(this.getClass().equals(animals.get(a).getClass())) {
    			if(this.getSex()!=animals.get(a).getSex()) {
    				return true;
    			}
    		}
    	}
    	return false;
    }
        
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    private int breed()
    {
        int births = 0;
        if(canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }

    /**
     * A rabbit can breed if it has reached the breeding age.
     * @return true if the rabbit can breed, false otherwise.
     */
    private boolean canBreed()
    {
        return age >= BREEDING_AGE;
    }
    /**
     * get the age of the rabbit
     * @return age
     */
    public int getAge()
    {
    	return age;
    }
}
