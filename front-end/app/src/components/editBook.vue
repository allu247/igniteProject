<template>
  <div class="container-fluid">
    <div class="row justify-content-center">
      <div class="col-xl-7 col-lg-8 col-md-9 col-sm-11">

        <h3>Edit book</h3>

        <form id="edit-form" @submit.prevent="saveEditBook" data-vv-scope="editing">

          <div class="form-group text-left">
            <label for="topic">Topic:</label>
            <select class="form-control" id="topic" name="topic" v-model="topic" v-validate="{ required: true }">
              <option value="Mathematics">Mathematics</option>
              <option value="Physics">Physics</option>
              <option value="Chemistry">Chemistry</option>
              <option value="Biology">Biology</option>
              <option value="Computer_Science">Computer Science</option>
              <option value="Varia" selected="selected">Varia</option>
            </select>
            <div class="error" v-if="errors.has('editing.topic')">{{errors.first('editing.topic')}}</div>
          </div>

          <div class="form-group text-left">
            <label for="title">Title:</label>
            <input id="title" class="form-control" type="text" name="title" placeholder="Title"
                   v-model="title" v-validate="{ required: true, min: 3, max: 128 }">
            <div class="error" v-if="errors.has('editing.title')">{{errors.first('editing.title')}}</div>
          </div>

          <div class="form-group text-left">
            <label for="author">Author:</label>
            <input class="form-control" id="author"
                   name="author" placeholder="Author"
                   v-model="author" v-validate="{ required: true, min: 3 }">
            <div class="error" v-if="errors.has('editing.author')">{{errors.first('editing.author')}}</div>
          </div>

          <div class="form-group text-left">
            <label for="ISBN">ISBN:</label>
            <input id="ISBN" class="form-control" type="text" name="ISBN"
                   placeholder="ISBN" v-model="ISBN" v-validate="{ required: true, min: 3 }">
            <div class="error" v-if="errors.has('editing.ISBN')">{{errors.first('editing.ISBN')}}</div>
          </div>


          <input class="btn btn-lg btn-primary mb-3" type="submit" value="Edit">
        </form>

        <div class="post-item text-left p-2 mx-2 mb-2" v-for="answer in response.comments" :key='answer.id'>
          <div class="text-left">
            <p class="mb-1">{{answer.comment}}</p>
            <p class="mt-2 mb-1">
              <small>
                Added: {{ answer.postedAt | moment("dddd, MMMM Do YYYY h:mm:ss a") }}
              </small>
            </p>
          </div>
        </div>

        <form class="my-3 p-2 reply-area" id="comment-form" @submit.prevent="postComment" data-vv-scope="commenting">

          <div class="form-group text-left">
            <label for="exampleFormControlTextarea1">Add comment to this book:</label>
            <textarea class="form-control" id="exampleFormControlTextarea1" rows="3" name="comment" placeholder="Comment"
                      v-model="comment" v-validate="{ required: true, min: 5 }"></textarea>
            <div class="error" v-if="errors.has('commenting.comment')">{{errors.first('commenting.comment')}}</div>
          </div>
          <input class="btn btn-lg btn-primary" type="submit" value="Add comment">
        </form>

      </div>
      <br>
    </div>
  </div>
</template>

<script>
    import apiRequests from '../javascript/apiRequests.js';
    import errorHandling from './../javascript/errorHandling.js';

    export default {
        name: 'viewpost',
        data() {
            return {
                response: [],
                topic: 'Varia',
                title: '',
                author: '',
                ISBN: '',
                comment: '',

            };
        },
        methods: {

            resetFields() {
                this.comment = '';
                this.$nextTick(() => this.$validator.reset())
            },
            loadPost() {
                apiRequests
                    .getRequestToApi('/api/books/' + this.$route.params.Pid)
                    .then((response) => {
                        this.response = response.data;
                        this.topic = response.data.topic;
                        this.title = response.data.title;
                        this.author = response.data.author;
                        this.ISBN = response.data.isbn;
                    })
                    .catch(() => {
                            errorHandling.errorMsgWithButton("This book no longer exists!")
                        }
                    );
            },

          saveEditBook() {
            this.$validator.validateAll("editing").then(valid => {
              if (!valid) {
                errorHandling.errorMsg("These changes are forbidden!", 1000);
              } else {
                apiRequests
                        .patchRequestToApi('/api/edit/book/' + this.$route.params.Pid, {
                          topic: this.topic,
                          title: this.title,
                          author: this.author,
                          isbn: this.ISBN


                        })
                        .then(() => {
                                  errorHandling.successMsg("Book successfully edited!", 1200);
                                  this.resetFields();
                              })
                        .catch(() => {
                                      errorHandling.errorMsgWithButton("Failed to update this book!")
                                    }
                            );
              }
            });
          },

            postComment() {
            this.$validator.validateAll("commenting").then(valid => {
                if (valid) {
                    apiRequests
                        .postRequestToApi('/api/add/comment', {
                                bookId: this.$route.params.Pid,
                                comment: this.comment,

                            }
                        )
                        .then(() => {
                            this.resetFields();
                            this.loadPost();
                        })
                        .catch((error) => {

                            errorHandling.errorMsgWithButton("Sorry, " +
                                "there was a problem and the comment couldn't be uploaded!");
                        });
                }
            })
            }


        },



        mounted() {
            this.loadPost();
        }
    }
</script>

<style scoped>
  .post-item {
    background-color: #f9f9f9;
    border-left: 4px solid #e9e9e9;
  }

  .reply-area {
    background-color: #f9f9f9;
  }

  .custom-file-upload {
    border: 2px solid gray;
    color: gray;
    background-color: white;
    padding: 8px 20px;
    border-radius: 8px;
    font-size: 20px;
    font-weight: bold;
  }

  input[type=file] {
    display: none;
  }
</style>
