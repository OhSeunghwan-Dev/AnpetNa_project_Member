package com.anpetna.member.refreshToken.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTokenEntity is a Querydsl query type for TokenEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTokenEntity extends EntityPathBase<TokenEntity> {

    private static final long serialVersionUID = -1485408925L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTokenEntity tokenEntity = new QTokenEntity("tokenEntity");

    public final DateTimePath<java.time.Instant> expiresAt = createDateTime("expiresAt", java.time.Instant.class);

    public final com.anpetna.member.domain.QMemberEntity member;

    public final StringPath memberId = createString("memberId");

    public final StringPath refreshToken = createString("refreshToken");

    public final DateTimePath<java.time.Instant> revokedAt = createDateTime("revokedAt", java.time.Instant.class);

    public final NumberPath<Long> tokenNo = createNumber("tokenNo", Long.class);

    public QTokenEntity(String variable) {
        this(TokenEntity.class, forVariable(variable), INITS);
    }

    public QTokenEntity(Path<? extends TokenEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTokenEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTokenEntity(PathMetadata metadata, PathInits inits) {
        this(TokenEntity.class, metadata, inits);
    }

    public QTokenEntity(Class<? extends TokenEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.anpetna.member.domain.QMemberEntity(forProperty("member")) : null;
    }

}

