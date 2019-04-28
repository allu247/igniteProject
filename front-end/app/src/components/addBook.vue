<template>
  <div class="container-fluid">
    <div class="row justify-content-center">
      <div class="col-xl-5 col-lg-6 col-md-8 col-sm-11">

        <h3>Add a book</h3>

        <form id="post-form" @submit.prevent="postFormData">

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
            <div class="error" v-if="errors.has('topic')">{{errors.first('topic')}}</div>
          </div>

          <div class="form-group text-left">
            <label for="title">Title:</label>
            <input id="title" class="form-control" type="text" name="title" placeholder="Title"
                   v-model="title" v-validate="{ required: true, min: 3, max: 128 }">
            <div class="error" v-if="errors.has('title')">{{errors.first('title')}}</div>
          </div>

          <div class="form-group text-left">
            <label for="author">Author:</label>
            <input class="form-control" id="author"
                      name="author" placeholder="Author"
                      v-model="author" v-validate="{ required: true, min: 3 }">
            <div class="error" v-if="errors.has('author')">{{errors.first('author')}}</div>
          </div>

          <div class="form-group text-left">
            <label for="ISBN">ISBN:</label>
            <input id="ISBN" class="form-control" type="text" name="ISBN"
                   placeholder="ISBN" v-model="ISBN" v-validate="{ required: true, min: 3 }">
            <div class="error" v-if="errors.has('ISBN')">{{errors.first('ISBN')}}</div>
          </div>


          <input class="btn btn-lg btn-primary mb-3" type="submit" value="Add">
        </form>
      </div>
    </div>
  </div>
</template>


<script>
    import apiRequests from './../javascript/apiRequests.js';
    import errorHandling from './../javascript/errorHandling.js';

    export default {
        name: 'addbook',
        data() {
            return {
                topic: 'Varia',
                title: '',
                author: '',
                ISBN: '',

            };
        },
        methods: {

            resetFields() {
                this.topic = 'Varia';
                this.title = '';
                this.author = '';
                this.ISBN = '';
                this.$nextTick(() => this.$validator.reset())
            },
            postFormData() {
                this.$validator.validate().then(valid => {
                    if (valid) {
                        apiRequests
                            .postRequestToApi('/api/add/book', {
                                topic: this.topic,
                                title: this.title,
                                author: this.author,
                                isbn: this.ISBN,

                            })
                            .then(() => {
                                errorHandling.successMsg("Book successfully uploaded!", 1200);
                                this.resetFields();
                            })
                            .catch((error) => {

                                errorHandling.errorMsgWithButton("Sorry, " +
                                    "there was a problem and the book couldn't be added!");

                            });
                    }
                })
            },
        },
    };
</script>


<style scoped>
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
