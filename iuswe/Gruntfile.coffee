module.exports = (grunt) ->
  grunt.loadNpmTasks('grunt-contrib-concat')
  grunt.loadNpmTasks('grunt-contrib-watch')
  grunt.loadNpmTasks('grunt-bower-concat')
  grunt.loadNpmTasks('grunt-contrib-uglify')
  grunt.loadNpmTasks('grunt-contrib-less')
  grunt.loadNpmTasks('grunt-ngmin')

  @angularSource = './app'
  @publicFolder = './src/main/webapp/public'

  grunt.initConfig
    pkg: grunt.file.readJSON('package.json')

    watch:
      javascript:
        files: @angularSource + '/**/*.js'
        tasks: ['concat:distJavascript', 'ngmin:app', 'uglify:app']

    concat:
      distJavascript:
        src: [@angularSource + "/common/**/*.js", @angularSource + "/services/**/*.js", @angularSource + "/controllers/**/*.js", @angularSource + "/directives/**/*.js", @angularSource + "/config/**/*.js"]
        dest: @publicFolder + "/js/app.js"

    ngmin:
      app:
        src: ['./src/main/webapp/public/js/app.js'],
        dest: './src/main/webapp/public/js/app.js'


    bower_concat:
      all:
        dest: @publicFolder + '/js/lib.js',
        include: [
          'json3'
          'lodash'
          'jquery'
          'intro.js'
          'jcrop'
          'angular'
          'angular-resource'
          'angular-cookies'
          'angular-sanitize'
          'angular-route'
          'angular-animate'
          'angular-bootstrap'
          'angular-dialog-service'
          'ng-tags-input'
          'angular-intro.js'
          'angular-promise-tracker'
          'showdown'
        ],
        mainFiles:
          'jcrop': 'js/jquery.Jcrop.js'
          'showdown': 'src/showdown.js'
        bowerOptions:
          relative: false

    uglify:
      lib:
        files:
          './src/main/webapp/public/js/lib.min.js': [@publicFolder + '/js/lib.js']
      app:
        files:
          './src/main/webapp/public/js/app.min.js': [@publicFolder + '/js/app.js']

    grunt.registerTask 'lib', 'contatinate and minify bower libs', ->
      grunt.task.run('bower_concat')
      grunt.task.run('uglify:lib')