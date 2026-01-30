package com.udacity.asteroidradar.data.mapper

import com.udacity.asteroidradar.api.getTodayDate
import com.udacity.asteroidradar.data.source.local.entity.ImageOfDayEntity
import com.udacity.asteroidradar.data.source.remote.dto.ImageOfDayDto
import com.udacity.asteroidradar.domain.model.ImageOfDayModel

/**
 * Mappers for converting between ImageOfDay models across different layers
 * Only two conversions needed:
 * - Entity -> Domain (for reading from database)
 * - DTO -> Entity (for writing API response to database)
 */

// Entity -> Domain (used when reading from database)
fun ImageOfDayEntity.toDomain(): ImageOfDayModel {
    return ImageOfDayModel(
        title = title,
        url = url,
        mediaType = mediaType,
        date = date
    )
}

// DTO -> Entity (used when saving API response to database)
fun ImageOfDayDto.toEntity(creationDate: String = getTodayDate()): ImageOfDayEntity {
    return ImageOfDayEntity(
        title = title,
        url = url,
        mediaType = mediaType,
        date = date,
        creationDate = creationDate
    )
}

