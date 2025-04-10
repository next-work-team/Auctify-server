package org.example.auctify.entity.user;

import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;
import org.example.auctify.dto.user.Role;
import org.example.auctify.entity.BaseTimeEntity;
import org.example.auctify.entity.bidHistory.BidHistoryEntity;
import org.example.auctify.entity.like.LikeEntity;
import org.example.auctify.entity.review.ReviewEntity;

import java.lang.annotation.ElementType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * worker : 조영흔
 * work : 유저에 관한 테이블을 생성해주는 엔티티 클래스
 * date    : 2025/02/17
 */

@Entity
@Table(name="users")
@Getter
@ToString()
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserEntity extends BaseTimeEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "oauth_id", nullable = false)
    private String oauthId;


    @Column(name = "nickName", nullable = true)
    private String nickName;

    @Column(name = "email", nullable = true)
    private String email;

    @Column(name = "birthday", nullable = true)
    private LocalDate birthday;


    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonIgnore
    private List<AddressEntity> address = new ArrayList<AddressEntity>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonIgnore
    private List<BidHistoryEntity> bidHistories = new ArrayList<BidHistoryEntity>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonIgnore
    private List<LikeEntity> like = new ArrayList<LikeEntity>();

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "image", nullable = true)
    private String image;

    @Column(name = "introduction", nullable = true)
    private String introduction;

    @OneToMany(mappedBy = "receiverUser", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ReviewEntity> receivedReviews = new ArrayList<ReviewEntity>();

    @OneToMany(mappedBy = "writerUser", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ReviewEntity> writtenReviews = new ArrayList<ReviewEntity>();


    public void onChangeEmail(String email) {
        this.email = email;
    }

    public void onChangeNickname(String nickname){this.nickName = nickname;}

    public void onChangeProfileImage(String image){this.image = image;}

    public void onChangeBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public void onChangeDefaultAddress(Long defaultAddressId) {
        this.address.forEach(addr ->{
            addr.onChangeDefaultAddress(defaultAddressId.equals(addr.getAddressId()));
        });
    }

    public Double getAverageTemperature(){
        Double averageTemperature = 0.0;
        if(!receivedReviews.isEmpty()) {
            averageTemperature = receivedReviews.stream()
                    .mapToDouble(ReviewEntity::getTemperature)
                    .average()
                    .orElse(0.0);
        }
        return averageTemperature;
    }


}
