<template>
  <div class="login_container">
    <div class="login_box">
      <div class="avatar_box">

      </div>
    </div>
  </div>
</template>

<script>
export default {}
</script>
<style lang="less" scoped>
.login_container{
  background-color:#e5abbe;
  height: 100%;
}
.login_box{
  width: 450px;
  height: 300px;
  background-color:#fdeff2;
  border-radius: 3px;
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%,-50%);
}
.login_box .avatar_box {
  height: 130px;
  width: 130px;
  border: 1px solid #eee;
  border-radius: 50%;
  padding: 10px;
  box-shadow:0 0 10px #ddd;
  position: absolute;
  left: 50%;
  transform: translate(-50%,-50%);
  background-color: #fff;
  img {
    width: 100%;
    height: 100%;
    border-radius: 50%;
    background-color: #eee;
  }
}
</style>
