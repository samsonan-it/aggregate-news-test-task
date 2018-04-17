$(function () {
  console.log("feeds.js");

  $('#submit-filter-btn').on('click', function(e) {
    var titleFilter = $("#title-filter").val();

    $.ajax({
      url: contextUrl + 'news/fragment/?filter=' + titleFilter,
      method: 'GET'
    }).success(function (data) {
      $('#news_table_full').html(data);
    });

    e.preventDefault();
    
  });

  $('.delete_feed').on('click', function (e) {
    var feed_id = $(this).data('id');
    console.log('delete feed ' + feed_id);
    var $feedRow = $(this).closest('tr');

    $.ajax({
      url: contextUrl + 'feeds/' + feed_id,
      method: 'DELETE'
    }).success(function () {
      console.log('feed deleted: ' + feed_id);
      showNotification(true, '');
      
      $feedRow.remove();

    }).fail(function (xhr, statusText) {
      console.log('failed to delete feed: ' + feed_id);
      showNotification(false, statusText);
    });

  });

  function showNotification(isSuccess, msg) {

    var alertClass = 'alert-success';

    if (isSuccess) {
      msg = 'Feed successfully deleted. ' + msg;
    } else {
      msg = 'Error deleting feed. ' + msg;
      alertClass = 'alert-danger';
    }

    var notification = '<div class="alert alert-dismissible fade in ' + alertClass + '"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' + msg + '</div>';
    
    $notifications = $('#notifications');

    $notifications.html(notification);
    $notifications.alert();

  };


});
