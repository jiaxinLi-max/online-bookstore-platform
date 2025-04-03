<!--&lt;!&ndash;<template>&ndash;&gt;-->
<!--&lt;!&ndash;  <div class="product-detail">&ndash;&gt;-->
<!--&lt;!&ndash;    <h1>{{ product.title }}</h1>&ndash;&gt;-->
<!--&lt;!&ndash;    <img :src="product.cover" alt="Cover Image" class="cover-image" />&ndash;&gt;-->
<!--&lt;!&ndash;    <div class="price">价格: ¥{{ product.price }}</div>&ndash;&gt;-->
<!--&lt;!&ndash;    <div class="rating">评分: {{ product.rate }}</div>&ndash;&gt;-->
<!--&lt;!&ndash;    <p class="description">描述: {{ product.description }}</p>&ndash;&gt;-->
<!--&lt;!&ndash;    <p class="detail">详情: {{ product.detail }}</p>&ndash;&gt;-->

<!--&lt;!&ndash;&lt;!&ndash;    <h3>书籍信息</h3>&ndash;&gt;&ndash;&gt;-->
<!--&lt;!&ndash;&lt;!&ndash;    <ul>&ndash;&gt;&ndash;&gt;-->
<!--&lt;!&ndash;&lt;!&ndash;      <li>作者: {{ specifications.author }}</li>&ndash;&gt;&ndash;&gt;-->
<!--&lt;!&ndash;&lt;!&ndash;      <li>副标题: {{ specifications.subtitle }}</li>&ndash;&gt;&ndash;&gt;-->
<!--&lt;!&ndash;&lt;!&ndash;      <li>ISBN: {{ specifications.isbn }}</li>&ndash;&gt;&ndash;&gt;-->
<!--&lt;!&ndash;&lt;!&ndash;      <li>装帧: {{ specifications.binding }}</li>&ndash;&gt;&ndash;&gt;-->
<!--&lt;!&ndash;&lt;!&ndash;      <li>页数: {{ specifications.pages }}</li>&ndash;&gt;&ndash;&gt;-->
<!--&lt;!&ndash;&lt;!&ndash;      <li>出版社: {{ specifications.publisher }}</li>&ndash;&gt;&ndash;&gt;-->
<!--&lt;!&ndash;&lt;!&ndash;      <li>出版日期: {{ specifications.publicationDate }}</li>&ndash;&gt;&ndash;&gt;-->
<!--&lt;!&ndash;&lt;!&ndash;    </ul>&ndash;&gt;&ndash;&gt;-->
<!--&lt;!&ndash;    <p class="detail">详情: {{ product.detail }}</p>&ndash;&gt;-->

<!--&lt;!&ndash;    <h3>书籍信息</h3>&ndash;&gt;-->
<!--&lt;!&ndash;    <ul>&ndash;&gt;-->
<!--&lt;!&ndash;      <li v-for="(spec, index) in specifications" :key="index">&ndash;&gt;-->
<!--&lt;!&ndash;        {{ spec.item }}: {{ spec.value }}&ndash;&gt;-->
<!--&lt;!&ndash;      </li>&ndash;&gt;-->
<!--&lt;!&ndash;    </ul>&ndash;&gt;-->
<!--&lt;!&ndash;  </div>&ndash;&gt;-->
<!--&lt;!&ndash;</template>&ndash;&gt;-->

<!--&lt;!&ndash;<script lang="ts">&ndash;&gt;-->
<!--&lt;!&ndash;import { defineComponent, ref, onMounted } from 'vue';&ndash;&gt;-->
<!--&lt;!&ndash;import { useRoute, useRouter } from 'vue-router';&ndash;&gt;-->
<!--&lt;!&ndash;import { getProduct,Product } from '../../api/product.ts';&ndash;&gt;-->
<!--&lt;!&ndash;import { Specification } from "../../api/specification.ts"; // 导入 Specification 接口&ndash;&gt;-->
<!--&lt;!&ndash;const router = useRouter();&ndash;&gt;-->
<!--&lt;!&ndash;const specifications = ref<any[]>([]); // 规格信息的数组&ndash;&gt;-->
<!--&lt;!&ndash;export default defineComponent({&ndash;&gt;-->
<!--&lt;!&ndash;  name: 'ProductDetail',&ndash;&gt;-->
<!--&lt;!&ndash;  setup() {&ndash;&gt;-->
<!--&lt;!&ndash;    const route = useRoute();&ndash;&gt;-->
<!--&lt;!&ndash;    const product = ref({&ndash;&gt;-->
<!--&lt;!&ndash;      title: '',&ndash;&gt;-->
<!--&lt;!&ndash;      price: 0,&ndash;&gt;-->
<!--&lt;!&ndash;      rate: 0,&ndash;&gt;-->
<!--&lt;!&ndash;      description: '',&ndash;&gt;-->
<!--&lt;!&ndash;      cover: '',&ndash;&gt;-->
<!--&lt;!&ndash;      detail: '',&ndash;&gt;-->
<!--&lt;!&ndash;    });&ndash;&gt;-->

<!--&lt;!&ndash;    const specifications = ref({&ndash;&gt;-->
<!--&lt;!&ndash;      author: '',&ndash;&gt;-->
<!--&lt;!&ndash;      subtitle: '',&ndash;&gt;-->
<!--&lt;!&ndash;      isbn: '',&ndash;&gt;-->
<!--&lt;!&ndash;      binding: '',&ndash;&gt;-->
<!--&lt;!&ndash;      pages: 0,&ndash;&gt;-->
<!--&lt;!&ndash;      publisher: '',&ndash;&gt;-->
<!--&lt;!&ndash;      publicationDate: '',&ndash;&gt;-->
<!--&lt;!&ndash;    });&ndash;&gt;-->

<!--&lt;!&ndash;    const loadProductDetails = async (productId: number) => {&ndash;&gt;-->
<!--&lt;!&ndash;      try {&ndash;&gt;-->
<!--&lt;!&ndash;        const response = await getProduct(productId);&ndash;&gt;-->
<!--&lt;!&ndash;        const productData = response.data.data; // 假设响应的结构是 { code: 200, data: {...} }&ndash;&gt;-->
<!--&lt;!&ndash;        console.log("productData",productData);&ndash;&gt;-->

<!--&lt;!&ndash;        // 更新产品数据&ndash;&gt;-->
<!--&lt;!&ndash;        product.value = {&ndash;&gt;-->
<!--&lt;!&ndash;          title: productData.title,&ndash;&gt;-->
<!--&lt;!&ndash;          price: productData.price,&ndash;&gt;-->
<!--&lt;!&ndash;          rate: productData.rate,&ndash;&gt;-->
<!--&lt;!&ndash;          description: productData.description,&ndash;&gt;-->
<!--&lt;!&ndash;          cover: productData.cover,&ndash;&gt;-->
<!--&lt;!&ndash;          detail: productData.detail,&ndash;&gt;-->
<!--&lt;!&ndash;        };&ndash;&gt;-->


<!--&lt;!&ndash;        // 更新规格数据&ndash;&gt;-->
<!--&lt;!&ndash;        // 更新规格数据&ndash;&gt;-->
<!--&lt;!&ndash;        // if (productData.specifications && productData.specifications.length > 0) {&ndash;&gt;-->
<!--&lt;!&ndash;        //   const specs = productData.specifications.reduce((acc: any, spec: any) => {&ndash;&gt;-->
<!--&lt;!&ndash;        //     // 使用 item 的值作为键，将 value 存入对应的字段&ndash;&gt;-->
<!--&lt;!&ndash;        //     acc[spec.item] = spec.value;&ndash;&gt;-->
<!--&lt;!&ndash;        //     return acc;&ndash;&gt;-->
<!--&lt;!&ndash;        //   }, {});&ndash;&gt;-->
<!--&lt;!&ndash;        //   specifications.value = {&ndash;&gt;-->
<!--&lt;!&ndash;        //     // author: specs['作者'] || '',&ndash;&gt;-->
<!--&lt;!&ndash;        //     // subtitle: specs['副标题'] || '',&ndash;&gt;-->
<!--&lt;!&ndash;        //     // isbn: specs['ISBN'] || '',&ndash;&gt;-->
<!--&lt;!&ndash;        //     // binding: specs['装帧'] || '',&ndash;&gt;-->
<!--&lt;!&ndash;        //     // pages: specs['页数'] || '',&ndash;&gt;-->
<!--&lt;!&ndash;        //     // publisher: specs['出版社'] || '',&ndash;&gt;-->
<!--&lt;!&ndash;        //     // publicationDate: specs['出版日期'] || '',&ndash;&gt;-->
<!--&lt;!&ndash;        //   };&ndash;&gt;-->
<!--&lt;!&ndash;        // }&ndash;&gt;-->
<!--&lt;!&ndash;        specifications.value = productData.specifications || []; // 直接使用返回的规格数组&ndash;&gt;-->

<!--&lt;!&ndash;    } catch (error) {&ndash;&gt;-->
<!--&lt;!&ndash;        console.error('Error loading product details:', error);&ndash;&gt;-->
<!--&lt;!&ndash;      }&ndash;&gt;-->
<!--&lt;!&ndash;    };&ndash;&gt;-->
<!--&lt;!&ndash;    const productId = Number(route.params.productId);&ndash;&gt;-->

<!--&lt;!&ndash;    // 假设我们传递了产品 ID，例如 102&ndash;&gt;-->
<!--&lt;!&ndash;    onMounted(() => {&ndash;&gt;-->
<!--&lt;!&ndash;      loadProductDetails(productId);&ndash;&gt;-->
<!--&lt;!&ndash;    });&ndash;&gt;-->

<!--&lt;!&ndash;    return {&ndash;&gt;-->
<!--&lt;!&ndash;      product,&ndash;&gt;-->
<!--&lt;!&ndash;      specifications,&ndash;&gt;-->
<!--&lt;!&ndash;    };&ndash;&gt;-->
<!--&lt;!&ndash;  },&ndash;&gt;-->
<!--&lt;!&ndash;});&ndash;&gt;-->
<!--&lt;!&ndash;</script>&ndash;&gt;-->

<!--&lt;!&ndash;<style scoped>&ndash;&gt;-->
<!--&lt;!&ndash;.product-detail {&ndash;&gt;-->
<!--&lt;!&ndash;  padding: 20px;&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;.cover-image {&ndash;&gt;-->
<!--&lt;!&ndash;  max-width: 100%;&ndash;&gt;-->
<!--&lt;!&ndash;  height: auto;&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;.price {&ndash;&gt;-->
<!--&lt;!&ndash;  font-size: 20px;&ndash;&gt;-->
<!--&lt;!&ndash;  font-weight: bold;&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;.rating {&ndash;&gt;-->
<!--&lt;!&ndash;  color: goldenrod;&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;.description, .detail {&ndash;&gt;-->
<!--&lt;!&ndash;  margin: 10px 0;&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->
<!--&lt;!&ndash;</style>&ndash;&gt;-->
<!--<template>-->
<!--  <div class="product-detail">-->
<!--    <div class="product-info">-->
<!--      <div class="product-image">-->
<!--        <img :src="product.cover" alt="Cover Image" class="cover-image" />-->
<!--      </div>-->
<!--      <div class="product-details">-->
<!--        <h1>{{ product.title }}</h1>-->
<!--        <div class="price">价格: ¥{{ product.price }}</div>-->
<!--        <div class="rating">评分: {{ product.rate }}</div>-->
<!--        <p class="description">描述: {{ product.description }}</p>-->
<!--        <p class="detail">详情: {{ product.detail }}</p>-->
<!--        <h3>书籍信息</h3>-->
<!--        <ul>-->
<!--          <li v-for="(spec, index) in specifications" :key="index">-->
<!--            {{ spec.item }}: {{ spec.value }}-->
<!--          </li>-->
<!--        </ul>-->
<!--        <p v-if="specifications.length === 0">没有书籍信息可显示。</p>-->
<!--      </div>-->
<!--    </div>-->
<!--    <el-input-number v-model="quantity" :min="1" :max="maxQuantity" label="选择数量"></el-input-number>-->
<!--    <el-button type="primary" @click="addToCart">加入购物车</el-button>-->
<!--  </div>-->

<!--</template>-->

<!--<script lang="ts">-->
<!--import { defineComponent, ref, onMounted } from 'vue';-->
<!--import { useRoute } from 'vue-router';-->
<!--import { getProduct } from '../../api/product.ts';-->
<!--import { addCart } from '../../api/cart.ts';-->
<!--import { Specification } from "../../api/specification.ts"; // 导入 Specification 接口-->

<!--const quantity = ref(1);-->
<!--const maxQuantity = ref(10); // 这里可以根据实际库存动态设置-->
<!--const route = useRoute();-->
<!--const productId = Number(route.params.productId);-->
<!--const addToCart = async () => {-->
<!--  try {-->
<!--    const userId = sessionStorage.getItem('userId');-->
<!--    console.log("inCart",userId);-->
<!--    // const response = await axios.post('/api/cart', {-->
<!--    //   userId: userId,-->
<!--    //   productId: props.product.id, // 使用传入的商品ID-->
<!--    //   quantity: quantity.value-->
<!--    // });-->
<!--    const response = await addCart(userId,productId,quantity.value);-->
<!--    if (response.data.code === 200) {-->
<!--      // 提示成功，或者进行其他操作-->
<!--      alert('商品已成功加入购物车');-->
<!--    } else {-->
<!--      alert(response.data.msg || '添加失败，请重试');-->
<!--    }-->
<!--  } catch (error) {-->
<!--    console.error('添加商品到购物车失败:', error);-->
<!--    alert('添加失败，请重试');-->
<!--  }-->
<!--};-->
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

<!--    // 定义 specifications 类型-->
<!--    const specifications = ref<Specification[]>([]);-->

<!--    const loadProductDetails = async (productId: number) => {-->
<!--      try {-->
<!--        const response = await getProduct(productId);-->
<!--        const productData = response.data.data; // 假设响应的结构是 { code: 200, data: {...} }-->
<!--        console.log("productData", productData);-->

<!--        // 更新产品数据-->
<!--        product.value = {-->
<!--          title: productData.title,-->
<!--          price: productData.price,-->
<!--          rate: productData.rate,-->
<!--          description: productData.description,-->
<!--          cover: productData.cover,-->
<!--          detail: productData.detail,-->
<!--        };-->

<!--        // 更新规格数据，直接使用返回的规格数组-->
<!--        specifications.value = productData.specifications || []; // 赋值为数组-->

<!--      } catch (error) {-->
<!--        console.error('Error loading product details:', error);-->
<!--      }-->
<!--    };-->

<!--    const productId = Number(route.params.productId);-->

<!--    // 在组件挂载后加载产品详情-->
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

<!--&lt;!&ndash;<style scoped>&ndash;&gt;-->
<!--&lt;!&ndash;.product-detail {&ndash;&gt;-->
<!--&lt;!&ndash;  padding: 20px;&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;.cover-image {&ndash;&gt;-->
<!--&lt;!&ndash;  max-width: 50%; /* 将最大宽度设置为100% */&ndash;&gt;-->
<!--&lt;!&ndash;  height: auto;    /* 自动调整高度以保持纵横比 */&ndash;&gt;-->
<!--&lt;!&ndash;  display: block;  /* 使图片块级显示，避免下方出现空隙 */&ndash;&gt;-->
<!--&lt;!&ndash;  margin: 0 auto;  /* 可选：使图片在容器中居中显示 */&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;.price {&ndash;&gt;-->
<!--&lt;!&ndash;  font-size: 20px;&ndash;&gt;-->
<!--&lt;!&ndash;  font-weight: bold;&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;.rating {&ndash;&gt;-->
<!--&lt;!&ndash;  color: goldenrod;&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;.description, .detail {&ndash;&gt;-->
<!--&lt;!&ndash;  margin: 10px 0;&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->
<!--&lt;!&ndash;</style>&ndash;&gt;-->
<!--<style scoped>-->
<!--.product-detail {-->
<!--  padding: 20px;-->
<!--  display: flex;-->
<!--  justify-content: center; /* 水平居中 */-->
<!--  align-items: center; /* 垂直居中 */-->
<!--  flex-direction: column; /* 使内容垂直排列 */-->
<!--}-->

<!--.product-info {-->
<!--  display: flex;-->
<!--  justify-content: center; /* 水平居中 */-->
<!--  align-items: flex-start; /* 垂直起始对齐 */-->
<!--  width: 100%;-->
<!--  max-width: 1200px; /* 限制最大宽度 */-->
<!--  margin: 0 auto; /* 水平居中 */-->
<!--}-->

<!--.product-image {-->
<!--  flex: 1;-->
<!--  margin-right: 20px;-->
<!--}-->

<!--.product-image img {-->
<!--  max-width: 100%;-->
<!--  height: auto;-->
<!--  display: block;-->
<!--}-->

<!--.product-details {-->
<!--  flex: 2;-->
<!--  margin-left: 20px;-->
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
<!--    <el-input-number v-model="quantity" :min="1" :max="maxQuantity" label="选择数量"></el-input-number>-->
<!--    <el-button type="primary" @click="addToCart">加入购物车</el-button>-->
    <div class="action-area">
      <!-- 客户操作 -->
      <div v-if="role === 'CUSTOMER'" class="customer-actions">
        <el-input-number v-model="quantity" :min="1" :max="maxQuantity" label="选择数量"></el-input-number>
        <el-button type="primary" @click="addToCart">加入购物车</el-button>
      </div>

      <!-- 管理员操作 -->
      <div v-if="role === 'MANAGER'" class="manager-actions">
        <div>库存: {{ stockAmount }}</div>
        <div class="stock-control">
          <el-input-number v-model="newStock" :min="0" label="新库存"></el-input-number>
          <el-button type="warning" @click="updateStock">更新库存</el-button>
        </div>

        <div class="management-buttons">
          <el-button type="primary" @click="updateInfo">更新信息</el-button>
          <el-button type="danger" @click="deleteProduct">删除商品</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import {deleteTheProduct, getProduct, updateProductInfo, updateStockpile, getStockpile} from '../../api/product.ts';
import { addCart } from '../../api/cart.ts';
import { Specification } from "../../api/specification.ts";
import {ElMessage} from "element-plus"; // 导入 Specification 接口

export default defineComponent({
  name: 'ProductDetail',
  methods: {updateProductInfo},
  setup() {
    const route = useRoute();
    const productId = Number(route.params.productId);

    const role = ref(sessionStorage.getItem('role') || '');
    const newStock = ref(0);

    const stockAmount = ref(0);
    const stockFrozen = ref(0);

    // 商品数据
    const product = ref({
      title: '',
      price: 0,
      rate: 0,
      description: '',
      cover: '',
      detail: '',
    });

    // 规格信息
    const specifications = ref<Specification[]>([]);
    const quantity = ref(1);
    const maxQuantity = ref(10); // 这里可以根据实际库存动态设置

    // 加载产品详情
    const loadProductDetails = async (productId: number) => {
      try {
        const response = await getProduct(productId);
        const productData = response.data.data; // 假设响应的结构是 { code: 200, data: {...} }

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

    // 添加到购物车
    const addToCart = async () => {
      try {
        const userId = sessionStorage.getItem('userId');
        const response = await addCart(userId, productId, quantity.value);
        console.log("response.data.code",response.data.code);
        console.log(typeof response.data.code); // 打印类型
        if (response.data.code === "200") {
          alert('商品已成功加入购物车');
        } else {
          alert(response.data.msg || '添加失败?，请重试');
        }
      } catch (error) {
        console.error('添加商品到购物车失败:', error);
        alert('添加失败，请重试');
      }
    };

    // 在组件挂载后加载产品详情
    onMounted(() => {
      loadProductDetails(productId);

      getStock();
    });

    const updateInfo = async () => {
      try {
        const response = await updateProductInfo({
          id: productId.toString(),
          title: product.value.title,
          price: product.value.price,
          rate: product.value.rate,
          description: product.value.description,
          cover: product.value.cover,
          detail: product.value.detail,
          specifications: specifications.value,
        });
        if (response.data.code === '200') {
          ElMessage.success('商品信息更新成功');
          console.log(response.data);
        }
      } catch (error) {
        console.error('更新商品信息失败:', error);
      }
    };

    const deleteProduct = async () => {
      try {
        const response = await deleteTheProduct(productId.toString());
        if (response.data.code === '200') {
          ElMessage.success('商品删除成功');
          console.log(response.data);
        }
      } catch (error) {
        console.error('删除商品失败:', error);
      }
    };

    const updateStock = async () => {
      try {
        const response = await updateStockpile(
          productId.toString(),
          newStock.value
        );
        if (response.data.code === '200') {
          stockAmount.value = newStock.value;
          ElMessage.success('库存更新成功');
          console.log(response.data);
        }
      } catch (error) {
        console.error('更新库存失败:', error);
      }
    };

    const getStock = async () => {
      try {
        const response = await getStockpile(productId.toString());
        if (response.data.code === '200') {
          stockAmount.value = response.data.data.amount;
          stockFrozen.value = response.data.data.frozen;
          console.log("StockAmount:" + response.data.data.amount);
          console.log("StockFrozen:" + response.data.data.frozen);
        }
      } catch (error) {
        console.error('获取库存信息失败:', error);
      }
    }

    return {
      product,
      specifications,
      quantity,
      maxQuantity,
      addToCart,
      role,
      newStock,
      updateInfo,
      deleteProduct,
      updateStock,
      getStock,
      stockAmount,
    };
  },
});
</script>

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

.action-area {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
}

.manager-actions {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
}
</style>
