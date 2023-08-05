package dev.easysouls.tracetrail.data.local

import dev.easysouls.tracetrail.domain.model.MissingPerson

fun MissingPersonEntity.toMissingPerson(): MissingPerson {
    return MissingPerson(
        id = id,
        firstName = firstName,
        lastName = lastName,
        lat = lat,
        lng = lng,
        uploadedBy = uploadedBy
    )
}

fun MissingPerson.toMissingPersonEntity(): MissingPersonEntity {
    return MissingPersonEntity(
        id = id,
        firstName = firstName,
        lastName = lastName,
        lat = lat,
        lng = lng,
        uploadedBy = uploadedBy
    )
}