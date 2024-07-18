package org.asphalt.lubricant.common

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
class Audit {
    @CreatedBy
    @Column(length = 100)
    var createdBy: String? = null

    @LastModifiedBy
    @Column(length = 100)
    var modifiedby: String? = null

    @CreatedDate
    var createdAt: LocalDateTime? = null
        protected set

    @LastModifiedDate
    var modifiedAt: LocalDateTime? = null
        protected set
}
