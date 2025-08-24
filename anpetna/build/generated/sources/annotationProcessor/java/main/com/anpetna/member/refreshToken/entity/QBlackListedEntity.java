package com.anpetna.member.refreshToken.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBlackListedEntity is a Querydsl query type for BlackListedEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBlackListedEntity extends EntityPathBase<BlackListedEntity> {

    private static final long serialVersionUID = -913356154L;

    public static final QBlackListedEntity blackListedEntity = new QBlackListedEntity("blackListedEntity");

    public final StringPath accessTokenHash = createString("accessTokenHash");

    public final NumberPath<Long> blackNo = createNumber("blackNo", Long.class);

    public final DateTimePath<java.time.Instant> expiresAt = createDateTime("expiresAt", java.time.Instant.class);

    public final StringPath refreshTokenHash = createString("refreshTokenHash");

    public QBlackListedEntity(String variable) {
        super(BlackListedEntity.class, forVariable(variable));
    }

    public QBlackListedEntity(Path<? extends BlackListedEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBlackListedEntity(PathMetadata metadata) {
        super(BlackListedEntity.class, metadata);
    }

}

