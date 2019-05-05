<template>
    <div>
        <div class="container-fluid blok1">
            <b-carousel
                    id="carousel-1"
                    v-model="slide"
                    :interval="4000"
                    controls
                    indicators
                    background="#ababab"
                    img-width="1024"
                    img-height="480"
                    style="text-shadow: 1px 1px 2px #333;"
                    class="d-block img-fluid w-100"
                    @sliding-start="onSlideStart"
            >
                <b-carousel-slide
                        caption="Start here"
                        text="Beautiful homes are here for you!"
                        img-src="https://firebasestorage.googleapis.com/v0/b/tarkvaratehnika-1551709647803.appspot.com/o/carousel1.jpg?alt=media&token=63d5eba9-7a88-4400-a520-678d063f603b"
                ></b-carousel-slide>

                <b-carousel-slide
                        img-src="https://firebasestorage.googleapis.com/v0/b/tarkvaratehnika-1551709647803.appspot.com/o/carousel2.jpg?alt=media&token=7039f720-9d8c-4590-99a7-69b347646a43">
                    <h1>Dream!</h1>
                </b-carousel-slide>

                <b-carousel-slide
                        img-src="https://firebasestorage.googleapis.com/v0/b/tarkvaratehnika-1551709647803.appspot.com/o/carousel3.jpg?alt=media&token=5ad8c87e-f168-42a0-9097-994f2d59b77b">
                    <h1>Find!</h1>
                </b-carousel-slide>

                <b-carousel-slide
                        img-src="https://firebasestorage.googleapis.com/v0/b/tarkvaratehnika-1551709647803.appspot.com/o/carousel4.jpg?alt=media&token=7d5fec2c-ffff-4750-992f-7f30943b26e1">
                    <h1>Rent!</h1>
                </b-carousel-slide>

            </b-carousel>


        </div>
        <div class="margin-top-image">
            <div class="container-fluid text-center">

                <div class="row" style="margin-top: 50px
">
                    <div class="col-md-2">
                    </div>
                    <div class="col-md-6 text-left">
                        <h1><b>You can start finding your <br> dream property </b></h1>
                        <p></p>
                        <h3 style="margin-top: 50px">Finding a new home is made very simple for you! <br> You only have
                            to choose and move</h3>
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
                            <input v-model="search" class="form-control form-control-sm ml-3 w-50" style="height: 60px"
                                   type="text" placeholder="Search the city"
                                   aria-label="Search">
                        </form>
                        <div class="ml-3 w-50">
                            <div>
                                <button type="button" class="btn btn-dark btn-lg btn-block"
                                        v-on:click.prevent=searchByCity>Start now
                                </button>
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
                            <h3 style="background-color: whitesmoke; padding: 30px">Add your property <br> to others to
                                find</h3>
                        </div>
                        <div class="ml-3 w-50">
                            <div>
                                <button type="button" class="btn btn-dark btn-lg btn-block"
                                        v-on:click.prevent=addProperty role="button">Start here
                                </button>
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
                slide: 0,
                sliding:true,
                search: "",
            }
        },
        methods: {


            searchByCity: function () {
                this.$store.dispatch('doGet', {url: 'property/search?municipality=' + this.search}).then(data => {
                    if (data.data.length !== 0) {
                        this.$router.push({path: `/all/${this.search}`});
                        //this.$router.go();
                    } else {
                        alert("There are no results that match your criteria")
                    }
                })

            },
            addProperty: function () {
                if (this.$store.getters.isLoggedIn) {
                    this.$router.push({path: `/property/add`});
                    //this.$router.go();
                } else {
                    this.$router.push({path: `/login`});
                    //this.$router.go();
                }
            },
            onSlideStart(slide) {
                this.sliding = true
            },



        }
    }
</script>

<style scoped>
    .emptyDiv {
        margin: 150px;
    }


    .active-cyan input[type=text] {
        border-bottom: 1px solid #4dd0e1;
        box-shadow: 0 1px 0 0 #4dd0e1;
    }

    .active-cyan .fa, .active-cyan-2 .fa {
        color: #4dd0e1;
    }


</style>
