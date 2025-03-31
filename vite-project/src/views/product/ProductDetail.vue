<template>
  <div class="product-detail">
    <h1>{{ product.title }}</h1>
    <img :src="product.cover" alt="Cover Image" class="cover-image" />
    <div class="price">价格: ¥{{ product.price }}</div>
    <div class="rating">评分: {{ product.rate }}</div>
    <p class="description">描述: {{ product.description }}</p>
    <p class="detail">详情: {{ product.detail }}</p>

    <h3>书籍信息</h3>
    <ul>
      <li>作者: {{ specifications.author }}</li>
      <li>副标题: {{ specifications.subtitle }}</li>
      <li>ISBN: {{ specifications.isbn }}</li>
      <li>装帧: {{ specifications.binding }}</li>
      <li>页数: {{ specifications.pages }}</li>
      <li>出版社: {{ specifications.publisher }}</li>
      <li>出版日期: {{ specifications.publicationDate }}</li>
    </ul>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { getProduct,Product } from '../../api/product.ts';

const router = useRouter();

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

    const specifications = ref({
      author: '',
      subtitle: '',
      isbn: '',
      binding: '',
      pages: 0,
      publisher: '',
      publicationDate: '',
    });

    const loadProductDetails = async (productId: number) => {
      try {
        const response = await getProduct(productId);
        const productData = response.data.data; // 假设响应的结构是 { code: 200, data: {...} }
        console.log("productData",productData);

        // 更新产品数据
        product.value = {

          title: productData.title,
          price: productData.price,
          rate: productData.rate,
          description: productData.description,
          cover: productData.cover,
          detail: productData.detail,
        };

        // 更新规格数据
        if (productData.specifications && productData.specifications.length > 0) {
          const specs = productData.specifications.reduce((acc: any, spec: any) => {
            acc[spec.item.toLowerCase()] = spec.value;
            return acc;
          }, {});
          specifications.value = specs;
        }
      } catch (error) {
        console.error('Error loading product details:', error);
      }
    };
    const productId = Number(route.params.productId);

    // 假设我们传递了产品 ID，例如 102
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

<style scoped>
.product-detail {
  padding: 20px;
}

.cover-image {
  max-width: 100%;
  height: auto;
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
