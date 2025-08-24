package com.anpetna.member.refreshToken.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTokenEntity is a Querydsl query type for TokenEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTokenEntity extends EntityPathBase<TokenEntity> {

    private static final long serialVersionUID = -1485408925L;

    public static final QTokenEntity tokenEntity = new QTokenEntity("tokenEntity");

    public final StringPath accessToken = createString("accessToken");

    public final StringPath id = createString("id");

    public final StringPath pw = createString("pw");

    public final StringPath refreshToken = createString("refreshToken");

    public final NumberPath<Long> tno = createNumber("tno", Long.class);

    public QTokenEntity(String variable) {
        super(TokenEntity.class, forVariable(variable));
    }

    public QTokenEntity(Path<? extends TokenEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTokenEntity(PathMetadata metadata) {
        super(TokenEntity.class, metadata);
    }

}

