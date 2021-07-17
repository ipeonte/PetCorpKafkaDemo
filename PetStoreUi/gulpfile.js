var gulp = require('gulp');

var clean = require('gulp-clean');
var concat = require('gulp-concat');
var nodemon = require('gulp-nodemon');
var livereload = require('gulp-livereload');
var minifyCss = require('gulp-minify-css');
var uglify = require('gulp-uglify');

var baseDirs = {
  app: './',
  dist: './dist/'
};

var publicDirs = {
  _self: 'public/',
  js: 'public/js/',
  css: 'public/css/',
  img: 'public/img/'
};

var bowerComponentsDir = baseDirs.app + 'bower_components/';

// Bower components first!
var appFiles = {
  js: [bowerComponentsDir + '**/*.min.js', baseDirs.app + 'js/**/*.js'],
  css: [bowerComponentsDir + '**/*.min.css', baseDirs.app + 'css/**/*.css'],
  index: [baseDirs.app + 'public/index.html']
};

var concatFilenames = {
  js: 'js.js',
  css: 'css.css'
};

var startupScript = 'server.js';

var sysDirs = [
  baseDirs.app + 'app/**/*.js',
  baseDirs.app + 'config/**/*.js',
  baseDirs.app + 'public/inde.html',
  baseDirs.app + 'node_modules/'
];

gulp.task('clean', function() {
  return gulp.src(baseDirs.dist, {read: false}).pipe(clean());
});

gulp.task('dev:concatjs', function () {
  return gulp.src(appFiles.js)
    .pipe(concat(concatFilenames.js))
    .pipe(gulp.dest(baseDirs.app + publicDirs.js));
});

gulp.task('dev:concatcss', function () {
  return gulp.src(appFiles.css)
    .pipe(concat(concatFilenames.css))
    .pipe(gulp.dest(baseDirs.app + publicDirs.css));
});

gulp.task('nodemon', function () {
  nodemon({
      script: baseDirs.app + startupScript,
      ext: 'js',
      ignore: [
        // baseDirs.app + 'public/',
        baseDirs.app + 'js/',
        baseDirs.app + 'css/']
    })
    .on('restart', function () {
      console.log('Magic restarted');
    });
});

gulp.task('livereload', function () {
	console.log(appFiles.index);
  return gulp.src(appFiles.index)
    .pipe(livereload());
});

gulp.task('watch', function () {
  livereload.listen();
  gulp.watch([
      appFiles.js,
      appFiles.css,
      baseDirs.app + 'public/index.html',
    ], ['livereload'])
    .on('change', function(event) {
      console.log('File ' + event.path + ' was ' + event.type + ', running tasks...');
    });
});

gulp.task('default', ['nodemon', 'watch']);
gulp.task('dist', ['dev:concatjs', 'dev:concatcss', 'dist:minifycss', 'dist:minifyjs', 'dist:copy']);

gulp.task('dist:minifycss', function() {
  return gulp.src(baseDirs.app + publicDirs.css + concatFilenames.css)
    .pipe(minifyCss())
    .pipe(gulp.dest(baseDirs.dist + publicDirs.css));
});

gulp.task('dist:minifyjs', function() {
  return gulp.src(baseDirs.app + publicDirs.js + concatFilenames.js)
    .pipe(uglify())
    .pipe(gulp.dest(baseDirs.dist + publicDirs.js));
});

gulp.task('dist:copy', function() {
  // server.js
  gulp.src(baseDirs.app + '/' + startupScript)
    .pipe(gulp.dest(baseDirs.dist));

  // sysDirs
  gulp.src(sysDirs, {cwd: baseDirs.app + '**'})
    .pipe(gulp.dest(baseDirs.dist));
});
