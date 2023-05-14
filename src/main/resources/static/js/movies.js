let currentMovieObject;


$(document).ready(function() {
  let debounceTimeout = null;
  $('#searchInput').on('input', function() {
    // clearTimeout stops debounceTimeout from executing its callback function
    clearTimeout(debounceTimeout);
    debounceTimeout = setTimeout(() => getMovie(this.value.trim()), 1500);
  })

  $('#showMore').on('click', function(e) {
    e.preventDefault();
    onShowMoreClicked();
  })

  // Java Exercise
  $('#favoriteLink').on('click', function() {
    addToFavorites();
  })

  $('#unfavoriteLink').on('click', function() {
    removeFromFavorites();
  })

  $('#logout').on('click', function() {
    logout();
  })
})

function logout() {
  window.location.href = "http://localhost:8080/logout"
  // let xhr = new XMLHttpRequest();
  // xhr.open("POST",'http://localhost:8080/logout');
  // xhr.onload = function() {
  //   if (xhr.status === 200) {
  //     window.location.href = '/login';
  //   } else {
  //     console.log('Logout request failed with status ' + xhr.status);
  //   }
  // };
  // xhr.send();
}

// Java Exercise
function addToFavorites() {
  let xhr = new XMLHttpRequest();
  xhr.open("PUT", `http://localhost:8080/api/user/favorites`, true);
  xhr.timeout = 5000;
  xhr.ontimeout = (e) => onApiError();
  xhr.onreadystatechange = function() {
    if (xhr.readyState === 4) {
      if (xhr.status === 200) {
        console.log("added to favorites")
      } else {
        onApiError();
      }
    }
  }
  xhr.setRequestHeader("Content-Type", "application/json")
  xhr.send(JSON.stringify({ "imdbID": Object.values(currentMovieObject)[18]}));
}

function removeFromFavorites() {
  let xhr = new XMLHttpRequest();
  xhr.open("DELETE", `http://localhost:8080/api/user/favorites`, true);
  xhr.timeout = 5000;
  xhr.ontimeout = (e) => onApiError();
  xhr.onreadystatechange = function() {
    if (xhr.readyState === 4) {
      if (xhr.status === 200) {
        console.log("removed from favorites")
      } else {
        onApiError();
      }
    }
  }
  xhr.setRequestHeader("Content-Type", "application/json")
  xhr.send(JSON.stringify({ "imdbID": Object.values(currentMovieObject)[18]}));
}

//
function getPrincipal() {
  let xhr = new XMLHttpRequest();
  xhr.open("GET",'http://localhost:8080/api/user/username');
  xhr.onload = function() {
    if (xhr.status === 200) {
      let username = xhr.responseText;
    } else {
      console.log('Request failed with status ' + xhr.status);
    }
  };
  xhr.send();

}

//
function getMovie(title) {
  if (!title) {
    return;
  }

  onBeforeSend();
  fetchMovieFromApi(title);
}

//
function fetchMovieFromApi(title) {
  let ajaxRequest = new XMLHttpRequest();
  ajaxRequest.open("GET", `http://omdbapi.com/?t=${title}&apikey=6b73f093`, true);
  ajaxRequest.timeout = 5000;
  ajaxRequest.ontimeout = (e) => onApiError();
  ajaxRequest.onreadystatechange = function() {
    if (ajaxRequest.readyState === 4) {
      if (ajaxRequest.status === 200) {
        console.log(JSON.parse(ajaxRequest.responseText))

        // Java Exercise
        currentMovieObject = JSON.parse(ajaxRequest.responseText)

        handleResults(JSON.parse(ajaxRequest.responseText));
      } else {
        onApiError();
      }
    }
  }
  ajaxRequest.send();
}

//
function handleResults(result) {
  if (result.Response === 'True') {
    let transformed = transformResponse(result);
    buildMovie(transformed);
  } else if (result.Response === 'False') {
    hideComponent('#waiting');
    showNotFound();
  }
}

// Assigns transformed API response to corresponding UI Elements
function buildMovie(apiResponse) {
  if (apiResponse.poster) {
    $('#image').attr('src', apiResponse.poster).on('load', function() {
      buildMovieMetadata(apiResponse, $(this))
    });
  } else {
    buildMovieMetadata(apiResponse);
  }
}

//
function onBeforeSend() {
  showComponent('#waiting');
  hideComponent('.movie');
  hideNotFound();
  //resetFavorite();
  hideError();
  collapsePlot();
  hideExtras();
}

//
function onApiError() {
  hideComponent('#waiting');
  showError();
}

//
function buildMovieMetadata(apiResponse, imageTag) {
  hideComponent('#waiting');
  handleImage(imageTag);
  handleLiterals(apiResponse);
  showComponent('.movie');
}

//
function handleImage(imageTag) {
  imageTag ? $('#image').replaceWith(imageTag) : $('#image').removeAttr('src');
}

//
function handleLiterals(apiResponse) {
  $('.movie').find('[id]').each((index, item) => {
    if ($(item).is('a')) {
      $(item).attr('href', apiResponse[item.id]);
    } else {
      let valueElement = $(item).children('span');
      let metadataValue = apiResponse[item.id] ? apiResponse[item.id] : '-';
      valueElement.length ? valueElement.text(metadataValue) : $(item).text(metadataValue);
    }
  })
}

//
function transformResponse(apiResponse) {
  let camelCaseKeysResponse = camelCaseKeys(apiResponse);
  clearNotAvailableInformation(camelCaseKeysResponse);
  buildImdbLink(camelCaseKeysResponse);
  return camelCaseKeysResponse;
}

//
function camelCaseKeys(apiResponse) {
  return _.mapKeys(apiResponse, (v, k) => _.camelCase(k));
}

//
function buildImdbLink(apiResponse) {
  if (apiResponse.imdbId && apiResponse.imdbId !== 'N/A') {
    apiResponse.imdbId = `https://www.imdb.com/title/${apiResponse.imdbId}`;
  }
}

//
function clearNotAvailableInformation(apiResponse) {
  for (var key in apiResponse) {
    if (apiResponse.hasOwnProperty(key) && apiResponse[key] === 'N/A') {
      apiResponse[key] = '';
    }
  }
}

//
function onShowMoreClicked() {
  $('#plot').toggleClass('expanded');
  if ($('.extended').is(':visible')) {
    $('.extended').hide(700);
  } else {
    $('.extended').show(700);
  }
}

//
function hideComponent(jquerySelector) {
  return $(jquerySelector).addClass('hidden');
}

//
function showComponent(jquerySelector) {
  return $(jquerySelector).removeClass('hidden');
}

//
function showNotFound() {
  $('.not-found').clone().removeClass('hidden').appendTo($('.center'));
}

function hideNotFound() {
  $('.center').find('.not-found').remove();
}

function showError() {
  $('.error').clone().removeClass('hidden').appendTo($('.center'));
}

function hideError() {
  $('.center').find('.error').remove();
}

function hideExtras() {
  $('.extended').hide();
}

function collapsePlot() {
  $('#plot').removeClass('expanded');
}