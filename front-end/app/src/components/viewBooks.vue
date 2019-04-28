<template>
  <div>
    <div class="container-fluid">
      <div class="row justify-content-center">

        <div class="col-lg-9 col-md-10 col-sm-12 px-0 divider">
          <div class="col-xl-4 col-lg-5 col-md-6 col-sm-8 input-group px-0 float-right order-by-form">
            <select class="custom-select border-0" id="inputGroupSelect" v-model="sortBy">
              <option disabled value="">Sort by...</option>
              <option>Latest posts first</option>
              <option>Earliest posts first</option>
              <option>Alphabetically descending</option>
              <option>Alphabetically ascending</option>
            </select>
            <div class="input-group-append border-0">
              <button class="btn btn-secondary border-0" type="button" @click="sort()">Sort</button>
            </div>
          </div>
        </div>

        <div class="col-lg-9 col-md-10 col-sm-12 px-0">
          <div class="post-list-item my-2 p-2 text-left" v-for="(data, index) in response" :key='data.id'>
            <h3>Title: {{data.title}}</h3>
            <p>Topic: {{data.topic}}</p>
            <p>Author: {{data.author}}</p>
            <p>ISBN: {{data.isbn}}</p>
              <p>Comments: {{data.comments.length}}</p>
            <button class="btn btn-danger m-1" type="submit"
                    @click="deletePost(data.id)">Delete</button>
            <button class="btn btn-warning m-1" type="submit"
                    @click="goToDetail(data.id)">Edit</button>
          </div>
        </div>
      </div>
      <router-link class="btn btn-dark mx-1" id="addNewBook" tag="button" to="/addbook" exact>
        Add book
      </router-link>
    </div>

    <br>
    <button class="mr-1 ml-1 mb-3 btn btn-lg btn-dark" v-if="currentPageNum > 0" v-on:click="prevPage()">
      Previous
    </button>
    <button class="mr-1 ml-1 mb-3 btn btn-lg btn-dark" v-if="currentPageNum < numOfPages" v-on:click="nextPage()">
      Next
    </button>
    <br>
  </div>
</template>

<script>
    import apiRequests from '../javascript/apiRequests.js';
    import errorHandling from './../javascript/errorHandling.js';

    export default {
        name: 'ViewPosts',
        methods: {
            loadContent() {
                this.currentPageNum = 0;

                this.searchTerm = this.$route.params.searchTerm;
                if (this.searchTerm !== null && this.searchTerm !== '' && this.searchTerm !== undefined) {
                    this.baseUrl = '/api/books/find?searchTerm=' + this.searchTerm;
                } else {
                    this.topic = this.$route.name;
                    this.baseUrl = '/api/books?topic=' + this.topic;
                }

                this.dataRequest(this.baseUrl);
            },
            goToDetail(proId) {
            this.$router.push({name: 'editbook', params: {Pid: proId}})
          },
            nextPage() {
                this.currentPageNum += 1;
                let url = this.getUrl() + '&page=' + this.currentPageNum;
                this.dataRequest(url);
            },
            prevPage() {
                this.currentPageNum -= 1;
                let url = this.getUrl() + '&page=' + this.currentPageNum;
                this.dataRequest(url);
            },
            getUrl() {
                let url = this.baseUrl;

                if (this.sortBy !== '') {
                    let sortKeyword;
                    let sortDirection;

                    if (this.sortBy === 'Latest posts first') {
                        sortKeyword = 'postedAt';
                        sortDirection = 'descending';
                    } else if (this.sortBy === 'Earliest posts first') {
                        sortKeyword = 'postedAt';
                        sortDirection = 'ascending';
                    } else if (this.sortBy === 'Alphabetically descending') {
                        sortKeyword = 'topic';
                        sortDirection = 'descending';
                    } else if (this.sortBy === 'Alphabetically ascending') {
                        sortKeyword = 'topic';
                        sortDirection = 'ascending';
                    }
                    url += '&sortBy=' + sortKeyword + '&order=' + sortDirection;
                }

                return url
            },
            sort() {
                this.dataRequest(this.getUrl());
            },

          deletePost(bookId) {
            apiRequests
                    .deleteRequestToApi('/api/delete/book/' + bookId)
                    .then(() => {
                      this.loadContent();
                    })
                    .catch(() => {
                              errorHandling.errorMsgWithButton("Failed to delete this book!")
                            }
                    );
          },

            dataRequest(url) {
                apiRequests.getRequestToApi(url)
                    .then(result => {
                        this.response = result.data.books;

                        if (result.data.amountOfPages <= 0) {
                            this.numOfPages = 0;
                        } else {
                            this.numOfPages = result.data.amountOfPages - 1;
                        }
                    });
            },
        },
        watch: {
            $route() {
                this.loadContent();
            }
        },
        data() {
            return {
                response: [],
                numOfPages: 0,
                currentPageNum: 0,
                topic: 'all',
                sortBy: '',
                searchTerm: null,
                baseUrl: ''
            };
        },
        mounted() {
            this.loadContent();
        }
    }
</script>

<style scoped>
  /*@import './../css/postsView.css';*/

  .divider {
    border-bottom: 4px solid #e5e5e5;
  }

  .order-by-form {
    border-top: 4px solid #e5e5e5;
    border-right: 4px solid #e5e5e5;
    border-left: 4px solid #e5e5e5;
    border-radius: 0;
    background-color: #f6f6f6;
  }

  .border-0 {
    border-radius: 0 !important;
  }
</style>
