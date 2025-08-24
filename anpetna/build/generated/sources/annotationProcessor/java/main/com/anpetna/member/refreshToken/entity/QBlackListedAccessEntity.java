package com.anpetna.member.refreshToken.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBlackListedAccessEntity is a Querydsl query type for BlackListedAccessEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBlackListedAccessEntity extends EntityPathBase<BlackListedAccessEntity> {

    private static final long serialVersionUID = 673352394L;

    public static final QBlackListedAccessEntity blackListedAccessEntity = new QBlackListedAccessEntity("blackListedAccessEntity");

    public final StringPath accessTokenHash = createString("accessTokenHash");

    public final NumberPath<Long> blackno = createNumber("blackno", Long.class);

    public final DateTimePath<java.time.Instant> expiresAt = createDateTime("expiresAt", java.time.Instant.class);

    public QBlackListedAccessEntity(String variable) {
        super(BlackListedAccessEntity.class, forVariable(variable));
    }

    public QBlackListedAccessEntity(Path<? extends BlackListedAccessEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBlackListedAccessEntity(PathMetadata metadata) {
        super(BlackListedAccessEntity.class, metadata);
    }

}

