export const hunhe = {
	methods: {
		showName(){
			alert(this.name)
		}
	},
	// 对于生命周期钩子，会一起使用组件里的和混入的
	mounted() {
		console.log('你好啊！')
	},
}
export const hunhe2 = {
	data() {
		return {
			x:100,
			y:200
		}
	},
}
