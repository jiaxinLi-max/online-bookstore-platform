<!--<template>-->
<!--  <div class="product-detail">-->
<!--    <h1>{{ product.title }}</h1>-->
<!--    <img :src="product.cover" alt="Cover Image" class="cover-image" />-->
<!--    <div class="price">价格: ¥{{ product.price }}</div>-->
<!--    <div class="rating">评分: {{ product.rate }}</div>-->
<!--    <p class="description">描述: {{ product.description }}</p>-->
<!--    <p class="detail">详情: {{ product.detail }}</p>-->

<!--&lt;!&ndash;    <h3>书籍信息</h3>&ndash;&gt;-->
<!--&lt;!&ndash;    <ul>&ndash;&gt;-->
<!--&lt;!&ndash;      <li>作者: {{ specifications.author }}</li>&ndash;&gt;-->
<!--&lt;!&ndash;      <li>副标题: {{ specifications.subtitle }}</li>&ndash;&gt;-->
<!--&lt;!&ndash;      <li>ISBN: {{ specifications.isbn }}</li>&ndash;&gt;-->
<!--&lt;!&ndash;      <li>装帧: {{ specifications.binding }}</li>&ndash;&gt;-->
<!--&lt;!&ndash;      <li>页数: {{ specifications.pages }}</li>&ndash;&gt;-->
<!--&lt;!&ndash;      <li>出版社: {{ specifications.publisher }}</li>&ndash;&gt;-->
<!--&lt;!&ndash;      <li>出版日期: {{ specifications.publicationDate }}</li>&ndash;&gt;-->
<!--&lt;!&ndash;    </ul>&ndash;&gt;-->
<!--    <p class="detail">详情: {{ product.detail }}</p>-->

<!--    <h3>书籍信息</h3>-->
<!--    <ul>-->
<!--      <li v-for="(spec, index) in specifications" :key="index">-->
<!--        {{ spec.item }}: {{ spec.value }}-->
<!--      </li>-->
<!--    </ul>-->
<!--  </div>-->
<!--</template>-->

<!--<script lang="ts">-->
<!--import { defineComponent, ref, onMounted } from 'vue';-->
<!--import { useRoute, useRouter } from 'vue-router';-->
<!--import { getProduct,Product } from '../../api/product.ts';-->
<!--import { Specification } from "../../api/specification.ts"; // 导入 Specification 接口-->
<!--const router = useRouter();-->
<!--const specifications = ref<any[]>([]); // 规格信息的数组-->
<!--export default defineComponent({-->
<!--  name: 'ProductDetail',-->
<!--  setup() {-->
<!--    const route = useRoute();-->
<!--    const product = ref({-->
<!--      title: '',-->
<!--      price: 0,-->
<!--      rate: 0,-->
<!--      description: '',-->
<!--      cover: '',-->
<!--      detail: '',-->
<!--    });-->

<!--    const specifications = ref({-->
<!--      author: '',-->
<!--      subtitle: '',-->
<!--      isbn: '',-->
<!--      binding: '',-->
<!--      pages: 0,-->
<!--      publisher: '',-->
<!--      publicationDate: '',-->
<!--    });-->

<!--    const loadProductDetails = async (productId: number) => {-->
<!--      try {-->
<!--        const response = await getProduct(productId);-->
<!--        const productData = response.data.data; // 假设响应的结构是 { code: 200, data: {...} }-->
<!--        console.log("productData",productData);-->

<!--        // 更新产品数据-->
<!--        product.value = {-->
<!--          title: productData.title,-->
<!--          price: productData.price,-->
<!--          rate: productData.rate,-->
<!--          description: productData.description,-->
<!--          cover: productData.cover,-->
<!--          detail: productData.detail,-->
<!--        };-->


<!--        // 更新规格数据-->
<!--        // 更新规格数据-->
<!--        // if (productData.specifications && productData.specifications.length > 0) {-->
<!--        //   const specs = productData.specifications.reduce((acc: any, spec: any) => {-->
<!--        //     // 使用 item 的值作为键，将 value 存入对应的字段-->
<!--        //     acc[spec.item] = spec.value;-->
<!--        //     return acc;-->
<!--        //   }, {});-->
<!--        //   specifications.value = {-->
<!--        //     // author: specs['作者'] || '',-->
<!--        //     // subtitle: specs['副标题'] || '',-->
<!--        //     // isbn: specs['ISBN'] || '',-->
<!--        //     // binding: specs['装帧'] || '',-->
<!--        //     // pages: specs['页数'] || '',-->
<!--        //     // publisher: specs['出版社'] || '',-->
<!--        //     // publicationDate: specs['出版日期'] || '',-->
<!--        //   };-->
<!--        // }-->
<!--        specifications.value = productData.specifications || []; // 直接使用返回的规格数组-->

<!--    } catch (error) {-->
<!--        console.error('Error loading product details:', error);-->
<!--      }-->
<!--    };-->
<!--    const productId = Number(route.params.productId);-->

<!--    // 假设我们传递了产品 ID，例如 102-->
<!--    onMounted(() => {-->
<!--      loadProductDetails(productId);-->
<!--    });-->

<!--    return {-->
<!--      product,-->
<!--      specifications,-->
<!--    };-->
<!--  },-->
<!--});-->
<!--</script>-->

<!--<style scoped>-->
<!--.product-detail {-->
<!--  padding: 20px;-->
<!--}-->

<!--.cover-image {-->
<!--  max-width: 100%;-->
<!--  height: auto;-->
<!--}-->

<!--.price {-->
<!--  font-size: 20px;-->
<!--  font-weight: bold;-->
<!--}-->

<!--.rating {-->
<!--  color: goldenrod;-->
<!--}-->

<!--.description, .detail {-->
<!--  margin: 10px 0;-->
<!--}-->
<!--</style>-->
<template>
  <div class="product-detail">
    <div class="product-info">
      <div class="product-image">
        <img :src="product.cover" alt="Cover Image" class="cover-image" />
      </div>
      <div class="product-details">
        <h1>{{ product.title }}</h1>
        <div class="price">价格: ¥{{ product.price }}</div>
        <div class="rating">评分: {{ product.rate }}</div>
        <p class="description">描述: {{ product.description }}</p>
        <p class="detail">详情: {{ product.detail }}</p>
        <h3>书籍信息</h3>
        <ul>
          <li v-for="(spec, index) in specifications" :key="index">
            {{ spec.item }}: {{ spec.value }}
          </li>
        </ul>
        <p v-if="specifications.length === 0">没有书籍信息可显示。</p>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { getProduct } from '../../api/product.ts';
import { Specification } from "../../api/specification.ts"; // 导入 Specification 接口

export default defineComponent({
  name: 'ProductDetail',
  setup() {
    const route = useRoute();
    const product = ref({
      title: '',
      price: 0,
      rate: 0,
      description: '',
      cover: '',
      detail: '',
    });

    // 定义 specifications 类型
    const specifications = ref<Specification[]>([]);

    const loadProductDetails = async (productId: number) => {
      try {
        const response = await getProduct(productId);
        const productData = response.data.data; // 假设响应的结构是 { code: 200, data: {...} }
        console.log("productData", productData);

        // 更新产品数据
        product.value = {
          title: productData.title,
          price: productData.price,
          rate: productData.rate,
          description: productData.description,
          cover: productData.cover,
          detail: productData.detail,
        };

        // 更新规格数据，直接使用返回的规格数组
        specifications.value = productData.specifications || []; // 赋值为数组

      } catch (error) {
        console.error('Error loading product details:', error);
      }
    };

    const productId = Number(route.params.productId);

    // 在组件挂载后加载产品详情
    onMounted(() => {
      loadProductDetails(productId);
    });

    return {
      product,
      specifications,
    };
  },
});
</script>

<!--<style scoped>-->
<!--.product-detail {-->
<!--  padding: 20px;-->
<!--}-->

<!--.cover-image {-->
<!--  max-width: 50%; /* 将最大宽度设置为100% */-->
<!--  height: auto;    /* 自动调整高度以保持纵横比 */-->
<!--  display: block;  /* 使图片块级显示，避免下方出现空隙 */-->
<!--  margin: 0 auto;  /* 可选：使图片在容器中居中显示 */-->
<!--}-->

<!--.price {-->
<!--  font-size: 20px;-->
<!--  font-weight: bold;-->
<!--}-->

<!--.rating {-->
<!--  color: goldenrod;-->
<!--}-->

<!--.description, .detail {-->
<!--  margin: 10px 0;-->
<!--}-->
<!--</style>-->
<style scoped>
.product-detail {
  padding: 20px;
  display: flex;
  justify-content: center; /* 水平居中 */
  align-items: center; /* 垂直居中 */
  flex-direction: column; /* 使内容垂直排列 */
}

.product-info {
  display: flex;
  justify-content: center; /* 水平居中 */
  align-items: flex-start; /* 垂直起始对齐 */
  width: 100%;
  max-width: 1200px; /* 限制最大宽度 */
  margin: 0 auto; /* 水平居中 */
}

.product-image {
  flex: 1;
  margin-right: 20px;
}

.product-image img {
  max-width: 100%;
  height: auto;
  display: block;
}

.product-details {
  flex: 2;
  margin-left: 20px;
}

.price {
  font-size: 20px;
  font-weight: bold;
}

.rating {
  color: goldenrod;
}

.description, .detail {
  margin: 10px 0;
}
</style>