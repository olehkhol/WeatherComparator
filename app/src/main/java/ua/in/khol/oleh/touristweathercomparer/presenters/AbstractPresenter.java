package ua.in.khol.oleh.touristweathercomparer.presenters;

import ua.in.khol.oleh.touristweathercomparer.model.Repository;

abstract class AbstractPresenter {
    Repository mRepository;

    AbstractPresenter() {

    }

    AbstractPresenter(Repository repository) {
        mRepository = repository;
    }


}
