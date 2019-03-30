<template>
  <div>
    <div class="container">
      <div class="image-widescreen">
        <img
          src="http://xdesktopwallpapers.com/wp-content/uploads/2012/02/Black%20And%20White%20Nice%20Apartment%20Interior.jpg"
          class="img-fluid" alt="Responsive image">
      </div>
    </div>
    <div class="margin-top-image">
      <div class="container-fluid text-center">

        <div class="row">
          <div class="col-md-2">
          </div>
          <div class="col-md-6 text-left">
            <h1><b>You can start finding your <br> dream property </b></h1>
            <p></p>
            <h3 style="margin-top: 50px">Finding a new home is made very simple for you! <br> You only have to choose and move</h3>
          </div>
          <div class="col-md-4">
          </div>
        </div>

        <div class="row">
          <div class="col-md-8 text-left">
          </div>
          <div class="col-md-4">
            <form class="form-inline active-cyan-3 active-cyan-4">
              <i class="fas fa-search" aria-hidden="true"></i>
              <input v-model="search" class="form-control form-control-sm ml-3 w-50" style="height: 60px" type="text" placeholder="Search the city"
                     aria-label="Search">
            </form>
            <div class="ml-3 w-50">
              <div>
                <button type="button" class="btn btn-dark btn-lg btn-block"  v-on:click.prevent=searchByCity>Start now</button>
              </div>
            </div>
          </div>
        </div>

        <div class="emptyDiv">
        </div>


        <div class="row">
          <div class="col-md-2">
          </div>
          <div class="col-md-5">
            <div class="ml-3 w-50">
              <h3 style="background-color: whitesmoke; padding: 30px">Add your property <br> to others to find</h3>
            </div>
            <div class="ml-3 w-50">
              <div>
                <button type="button" class="btn btn-dark btn-lg btn-block"  v-on:click.prevent=addProperty role="button">Start here</button>
              </div>
            </div>
          </div>
          <div class="col-md-5 text-left">
            <h1><b>Or do you want to <br> rent your own? </b></h1>
            <p></p>
            <h3>Convenient <br> Secure <br> Everything made simple! </h3>
          </div>
        </div>
      </div>
    </div>
  </div>

</template>

<script>

  export default {
    data() {
      return {
        search:"",
      }
    },
    methods:{


      searchByCity: function () {
        this.$store.dispatch('doGet', {url: 'property/search?municipality=' + this.search}).then(data => {
          if(data.data.length !== 0){
            this.$router.push({path: `/all/${this.search}`});
            this.$router.go();
          }
          else{
            //do nothing
          }
        })

      },
      addProperty: function () {
        if(this.$store.getters.isLoggedIn){
          this.$router.push({path: `/property/add`});
          this.$router.go();
        }
        else{
          this.$router.push({path: `/login`});
          this.$router.go();
        }
      }


    }
  }
</script>

<style scoped>

  .emptyDiv{
    margin: 300px;
  }


  .active-cyan input[type=text] {
    border-bottom: 1px solid #4dd0e1;
    box-shadow: 0 1px 0 0 #4dd0e1;
  }

  .active-cyan .fa, .active-cyan-2 .fa {
    color: #4dd0e1;
  }

  .image-widescreen {
    position: absolute;
    top: 60px;
    left: 0;
    width: 100%;
    min-height: 650px;
    max-height: 650px;
    overflow-y: hidden;
  }

  .image-widescreen img {
    width: 100%;
  }

  .margin-top-image {
    margin-top: 710px;
  }

</style>
