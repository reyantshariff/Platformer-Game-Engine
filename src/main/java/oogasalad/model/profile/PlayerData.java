package oogasalad.model.profile;

/**
 * This record represents the data of a player profile. It a representation of the data stored in the database
 *
 * @author Justin Aronwald
 */
public record PlayerData(
    String username,
    long createdAt
) {}