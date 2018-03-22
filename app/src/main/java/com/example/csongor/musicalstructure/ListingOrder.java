package com.example.csongor.musicalstructure;

import java.io.Serializable;

/**
 * Created by csongor on 3/5/18.
 * Enum for listing order.
 *
 */

public enum ListingOrder implements Serializable{
    ARTIST,
    GENRE,
    TITLE,
    LENGTH,
    DEFAULT
}
