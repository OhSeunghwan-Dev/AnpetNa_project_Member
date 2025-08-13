package com.anpetna.member.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMemberEntity is a Querydsl query type for MemberEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberEntity extends EntityPathBase<MemberEntity> {

    private static final long serialVersionUID = -1064612119L;

    public static final QMemberEntity memberEntity = new QMemberEntity("memberEntity");

    public final com.anpetna.coredomain.QBaseEntity _super = new com.anpetna.coredomain.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final StringPath emailStsYn = createString("emailStsYn");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> latestDate = _super.latestDate;

    public final StringPath memberBirthD = createString("memberBirthD");

    public final StringPath memberBirthGM = createString("memberBirthGM");

    public final StringPath memberBirthM = createString("memberBirthM");

    public final StringPath memberBirthY = createString("memberBirthY");

    public final StringPath memberDetailAddress = createString("memberDetailAddress");

    public final StringPath memberEmail = createString("memberEmail");

    public final StringPath memberEtc = createString("memberEtc");

    public final StringPath memberFileImage = createString("memberFileImage");

    public final StringPath memberGender = createString("memberGender");

    public final StringPath memberHasPet = createString("memberHasPet");

    public final StringPath memberId = createString("memberId");

    public final StringPath memberName = createString("memberName");

    public final StringPath memberPhone = createString("memberPhone");

    public final StringPath memberPw = createString("memberPw");

    public final StringPath memberRoadAddress = createString("memberRoadAddress");

    public final EnumPath<com.anpetna.member.constant.MemberRole> memberRole = createEnum("memberRole", com.anpetna.member.constant.MemberRole.class);

    public final BooleanPath memberSocial = createBoolean("memberSocial");

    public final StringPath memberZipCode = createString("memberZipCode");

    public final StringPath smsStsYn = createString("smsStsYn");

    public QMemberEntity(String variable) {
        super(MemberEntity.class, forVariable(variable));
    }

    public QMemberEntity(Path<? extends MemberEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMemberEntity(PathMetadata metadata) {
        super(MemberEntity.class, metadata);
    }

}

