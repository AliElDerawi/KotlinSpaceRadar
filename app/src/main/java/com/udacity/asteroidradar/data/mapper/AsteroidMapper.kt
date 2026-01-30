package com.udacity.asteroidradar.data.mapper

import com.udacity.asteroidradar.data.source.local.entity.AsteroidEntity
import com.udacity.asteroidradar.domain.model.AsteroidModel

/**
 * Mappers for converting between Asteroid models across different layers
 * Only two conversions needed:
 * - Entity -> Domain (for reading from database)
 * - Domain -> Entity (for writing to database)
 */

// Entity -> Domain
fun AsteroidEntity.toDomain(): AsteroidModel {
    return AsteroidModel(
        id = id,
        codename = codename,
        closeApproachDate = closeApproachDate,
        absoluteMagnitude = absoluteMagnitude,
        estimatedDiameter = estimatedDiameter,
        relativeVelocity = relativeVelocity,
        distanceFromEarth = distanceFromEarth,
        isPotentiallyHazardous = isPotentiallyHazardous
    )
}

// Domain -> Entity
fun AsteroidModel.toEntity(): AsteroidEntity {
    return AsteroidEntity(
        id = id,
        codename = codename,
        closeApproachDate = closeApproachDate,
        absoluteMagnitude = absoluteMagnitude,
        estimatedDiameter = estimatedDiameter,
        relativeVelocity = relativeVelocity,
        distanceFromEarth = distanceFromEarth,
        isPotentiallyHazardous = isPotentiallyHazardous
    )
}

