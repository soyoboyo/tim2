<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="en">
<head>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <link href="webjars/bootstrap/4.1.3/css/bootstrap.min.css" rel="stylesheet">
    <title>Simple Web App</title>
</head>
<body>
<div class="container-fluid">
    <form method="get" action="/locale">
        <div class="row">
            <div class="col-md-9"></div>
            <select class="custom-select custom-select-lg mb-3 col-md-1" style="height: 40px" name="locale">
                <c:forEach var="entry" items="${locales}">
                    <option selected>${entry.getName()}</option>
                </c:forEach>
            </select>
            <button  class="btn btn-primary col-md-2" type="submit" value="submit" style="height: 40px">
                Change language
            </button>
        </div>
        <hr style="margin-top: -15px">
    </form>

    <div class="d-flex flex-column flex-md-row align-items-center p-3 px-md-4 mb-3 bg-white border-bottom box-shadow"  style="margin-top: -15px">
        <h5 class="my-0 mr-md-auto font-weight-normal">${ocadoTechnology}</h5>
        <nav class="my-2 my-md-0 mr-md-3">
            <a class="p-2 text-dark" href="#">${featuresNav}</a>
            <a class="p-2 text-dark" href="#">${enterpriseNav}</a>
            <a class="p-2 text-dark" href="#">${supportNav}</a>
            <a class="p-2 text-dark" href="#">${pricingNav}</a>
        </nav>
        <a class="btn btn-outline-primary" href="#">${signUp}</a>
    </div>

    <div class="container">
        <div class="pricing-header px-3 py-3 pt-md-5 pb-md-4 mx-auto text-center">
            <h1 class="display-4">${pricing}</h1>
            <p class="lead">${welcome1} ${welcome2}</p>
        </div>
        <div class="container">
            <div class="card-deck mb-3 text-center">
                <div class="card mb-4 box-shadow">
                    <div class="card-header">
                        <h4 class="my-0 font-weight-normal">${freeCard}</h4>
                    </div>
                    <div class="card-body">
                        <h1 class="card-title pricing-card-title">$0 <small class="text-muted">/ mo</small></h1>
                        <ul class="list-unstyled mt-3 mb-4">
                            <li>${maxFreeUsers}</li>
                            <li>${maxFreeMemoryLimit}</li>
                            <li>${freeEmailSupport}</li>
                            <li>${freeHelpCenter}</li>
                        </ul>
                        <button type="button" class="btn btn-lg btn-block btn-outline-primary">${signUpToAccess}</button>
                    </div>
                </div>
                <div class="card mb-4 box-shadow">
                    <div class="card-header">
                        <h4 class="my-0 font-weight-normal">${proCard}</h4>
                    </div>
                    <div class="card-body">
                        <h1 class="card-title pricing-card-title">$15 <small class="text-muted">/ mo</small></h1>
                        <ul class="list-unstyled mt-3 mb-4">
                            <li>${maxProUsers}</li>
                            <li>${maxProMemoryLimit}</li>
                            <li>${proEmailSupport}</li>
                            <li>${proHelpCenter}</li>
                        </ul>
                        <button type="button" class="btn btn-lg btn-block btn-primary">${getStarted}</button>
                    </div>
                </div>
                <div class="card mb-4 box-shadow">
                    <div class="card-header">
                        <h4 class="my-0 font-weight-normal">${enterpriseCard}</h4>
                    </div>
                    <div class="card-body">
                        <h1 class="card-title pricing-card-title">$29 <small class="text-muted">/ mo</small></h1>
                        <ul class="list-unstyled mt-3 mb-4">
                            <li>${maxEnterpriseUsers}</li>
                            <li>${maxEnterpriseMemoryLimit}</li>
                            <li>${enterpriseEmailSupport}</li>
                            <li>${enterpriseHelpCenter}</li>
                        </ul>
                        <button type="button" class="btn btn-lg btn-block btn-primary">${contactUs}</button>
                    </div>
                </div>
            </div>

            <footer class="pt-4 my-md-5 pt-md-5 border-top">
                <div class="row">
                    <div class="col-6 col-md-4 text-center">
                        <h5>${featuresFooter}</h5>
                        <ul class="list-unstyled text-small">
                            <li><a class="text-muted" href="#">${featuresFooter1}</a></li>
                            <li><a class="text-muted" href="#">${featuresFooter2}</a></li>
                            <li><a class="text-muted" href="#">${featuresFooter3}</a></li>
                            <li><a class="text-muted" href="#">${featuresFooter4}</a></li>
                            <li><a class="text-muted" href="#">${featuresFooter5}</a></li>
                            <li><a class="text-muted" href="#">${featuresFooter6}</a></li>
                        </ul>
                    </div>
                    <div class="col-6 col-md-4 text-center">
                        <h5>${resourcesFooter}</h5>
                        <ul class="list-unstyled text-small">
                            <li><a class="text-muted" href="#">${resourcesFooter1}</a></li>
                            <li><a class="text-muted" href="#">${resourcesFooter2}</a></li>
                            <li><a class="text-muted" href="#">${resourcesFooter3}</a></li>
                            <li><a class="text-muted" href="#">${resourcesFooter4}</a></li>
                        </ul>
                    </div>
                    <div class="col-6 col-md-4 text-center">
                        <h5>${aboutFooter}</h5>
                        <ul class="list-unstyled text-small">
                            <li><a class="text-muted" href="#">${aboutFooter1}</a></li>
                            <li><a class="text-muted" href="#">${aboutFooter2}</a></li>
                            <li><a class="text-muted" href="#">${aboutFooter3}</a></li>
                            <li><a class="text-muted" href="#">${aboutFooter4}</a></li>
                        </ul>
                    </div>
                </div>
            </footer>
        </div>
    </div>
</div>
<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
</body>
</html>
