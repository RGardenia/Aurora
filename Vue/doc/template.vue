<template>
  <div className="container">
    {{ title }}
    #[[$END$]]#
  </div>
</template>

<script>
import "";
import xx from "";

export default {
  name: '${COMPONENT_NAME}',
  props: {
    title: {
      type: String,
      required: true
    },
    dataArr: {
      type: Array,
      required: false,
      default() {
        return [];
      },
    },
  },
  data() {
    return {}
  },
  components: {
    // 组件注册
  },
  beforeCreate() {
    // 在实例初始化之后，数据观测(data observer)和 event watcher 事件配置之前被调用
  },
  create() {
    // 实例已经创建完成之后被调用。
    // 在这一步，实例已完成以下的配置:数据观测(data observer),属性和方法的运算，watch/event 事件回调。
    // 然而，挂载阶段还没开始，$el 属性目前不可见。
  },
  beforeMount() {
    //在挂载开始之前被调用:相关的 render 函数首次被调用。
  },
  mounted() {
    // el 被新创建的vm.$el替换,并挂载到实例上去之后调用该钩子。
  },
  beforeUpdate() {
    //数据更新时调用，发生在虚拟 DOM 重新渲染和打补丁之前。你可以在这个钩子中进一步地更改状态,这不会触发附加的重渲梁过程。
  },
  updated() {
    //由于数据更改导致的虚拟DOM重新渲染和打补丁，在这之后会调用该钩子。
    //当这个钩子被调用时，组件 DOM 已经更新，所以你现在可以执行依赖于DOM的操作。
    // 然而在大多数情况下，你应该避免在此期间更改状态，因为这可能会导致更新无限循环
  },
  beforeDestroy() {
    //实例销毁之前调用。在这一步，实例仍然完全可用。
  },
  destroyed() {
    // Vue实例销毁后调用。调用后，Vue 实例指示的所有东西都会解绑定，所有的事件监听器会被移除，所有的子实例也会被销毁。
    // 该钩子在 服务器喘渲染期间不被调用。
  },
  computed: {
    //计算展性
  },
  watch: {
    //数据监听
  },
  methods: {
    // 方法定义
  }
}
</script>

<style scoped>
/*@import "";*/

</style>