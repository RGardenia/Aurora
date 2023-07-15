<template>
	<div>
		<!-- 加 : 号，传值的是双引号内的条件表达式 -->
		<Student name="李四" sex="女" :age="18"/>
	</div>
</template>

<script>
	import Student from './components/Student'

	export default {
		name:'App',
		components:{Student}
	}
</script>
