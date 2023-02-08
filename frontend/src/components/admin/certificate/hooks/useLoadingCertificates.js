import {useEffect} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {useSearchParams} from 'react-router-dom';
import {
  changeStatus,
  loadCertificates,
  setPageByUrl,
} from '../../../../store/certificates/certificateSlice';
import {selectFilter} from '../../../../store/filter/filterSelectors';

export const useLoadingCertificates = status => {
  const dispatch = useDispatch();
  const filter = useSelector(selectFilter);
  const [searchParams, setSearchParams] = useSearchParams();
  const isSuccessMessageShow =
    status === 'created' || status === 'updated' || status === 'deleted';
  const pageNumber = searchParams.get('page') || 1;
  const size = searchParams.get('size') || 20;

  useEffect(() => {
    dispatch(setPageByUrl({pageNumber, size}));
  }, [dispatch, filter, pageNumber, size]);

  useEffect(() => {
    if (status === 'idle') {
      dispatch(
        loadCertificates({
          number: pageNumber - 1,
          size,
          filter,
          infinite: false,
        })
      );
    }
  }, [pageNumber, size, filter, dispatch, status]);

  useEffect(() => {
    if (isSuccessMessageShow) {
      dispatch(changeStatus('idle'));
    }
  }, [dispatch, isSuccessMessageShow]);

  const changePage = number => {
    setSearchParams(prev => {
      if (number > 0) {
        prev.set('page', number);
      } else {
        prev.delete('page');
      }

      return prev;
    });
  };

  const changeSize = newSize => {
    setSearchParams(prev => {
      if (newSize !== 20) {
        prev.set('size', newSize);
      } else {
        prev.delete('size');
      }
      prev.delete('page');

      return prev;
    });
  };

  return {isSuccessMessageShow, changePage, changeSize};
};
