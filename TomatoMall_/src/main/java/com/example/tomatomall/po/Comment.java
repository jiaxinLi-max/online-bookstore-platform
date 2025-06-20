package com.example.tomatomall.po;

import com.example.tomatomall.vo.CommentVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 主键自增
    @Column(name = "id")
    private Integer id;

    // 评论的用户 ID（对应哪个用户发布了该评论）
    @Basic
    @Column(name = "userId")
    private Integer userId;

    // 评论关联的商品 ID（即评论是评价哪个商品的）
    @Basic
    @Column(name = "productId")
    private Integer productId;

    // 评论内容文本
    @Basic
    @Column(name = "content")
    private String content;

    // 评论的评分（例如 1~5 星）
    @Basic
    @Column(name = "score")
    private Float score;

    // 评论的点赞数
    @Basic
    @Column(name = "likes")
    private Integer likes;

    // 记录点赞用户 ID 集合，用于防止重复点赞或实现用户撤回点赞等功能
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Integer> likedUserIds = new HashSet<>();

    // 评论发布时间（不可为空）
    @Basic
    @Column(name = "time", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime time;

    // ========== 【父子评论结构：核心设计】 ==========

    /**
     * 该字段表示“当前评论”的父评论（即它是对哪一条评论的回复）
     *
     * - @ManyToOne 表示多个子评论可以对应同一个父评论（多对一）
     * - fetch = LAZY：默认不立即加载父评论内容，避免性能开销
     * - @JoinColumn(name = "parent_id") 表示该字段映射到数据库中的 parent_id 字段
     * - 若该字段为 null，则表示它是顶层评论；若非 null，则表示它是某条评论的回复
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")  // 数据库中对应的字段是 parent_id
    private Comment parent;

    /**
     * 该字段表示“当前评论”的所有子评论（即其他人对它的回复）
     *
     * - @OneToMany 表示一个评论可能有多个回复（即一对多）
     * - mappedBy = "parent"：说明关系的维护由 `parent` 字段负责，不会生成额外的中间表
     * - cascade = ALL：对子评论的新增、删除操作会级联到数据库
     * - orphanRemoval = true：当某个子评论被移除时，也会从数据库中删除
     *
     * 注意：这是一个双向关联关系，和 `parent` 字段配对，实现完整的父子嵌套结构
     */
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> replies = new HashSet<>();



    /**
     * 将 Comment 实体对象转换为前端展示所需的 CommentVO 对象。
     *
     * VO 是 View Object，专用于控制层（Controller）向前端传输数据，避免直接暴露数据库结构。
     */
    public CommentVO toVO() {
        CommentVO commentVO = new CommentVO();

        commentVO.setId(this.id);
        commentVO.setProductId(this.productId);
        commentVO.setUserId(this.userId);
        commentVO.setScore(this.score);
        commentVO.setContent(this.content);
        commentVO.setLikes(this.likes);
        commentVO.setTime(this.time);

        // 如果有父评论，则设置其 ID，否则设置为 null（说明是顶层评论）
        commentVO.setParentId(parent != null ? parent.getId() : null);

        return commentVO;
    }
}
