<template>
  <div>
    <h2>Login</h2>
    <form>
      <div class="form-group">
        <label>Email</label>
        <input type="text" v-model="input.username" class="form-control" />
        <!--<div v-show="submitted && !username" class="invalid-feedback">Username is required</div>-->
      </div>
      <div class="form-group">
        <label htmlFor="password">Password</label>
        <input type="password" v-model="input.password" class="form-control" />
        <!--<div v-show="submitted && !password" class="invalid-feedback">Password is required</div>-->
      </div>
      <div class="form-group">
        <button class="btn btn-primary" v-on:click.prevent="login">Login</button>
        <router-link to="/register" class="btn btn-link">Register</router-link>
      </div>
    </form>
  </div>
</template>

<script>
    export default {
        name: "login",
        data() {
          return {
            input: {
              username: "",
              password: ""
            }
          }
        },
        methods: {
          login() {
            const self = this;

            let resource = self.$resource('someItem{/id}', {}, {
                post: {method: 'POST', url: 'http://localhost:8080/api/login'}
              });

              // POST someItem/baz
              resource.post({
                username: self.input.username,
                password: self.input.password
              }
            ).then(response => {
                console.log(response);
                console.log("succcess");
              }, response => {
                console.log(response);
              });
            console.log(self.input.username + " and " + self.input.password);
          }
        }
    }
</script>

<style scoped>

</style>
