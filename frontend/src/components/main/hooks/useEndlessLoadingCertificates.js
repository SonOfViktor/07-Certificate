import {useEffect, useRef} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {
  selectCertificatesInfo,
  selectCertificatesPage,
} from '../../../store/certificates/certificateSelectors';
import {
  loadCertificates,
  resetCertificates,
  setPage,
} from '../../../store/certificates/certificateSlice';
import {selectFilter} from '../../../store/filter/filterSelectors';

export const useEndlessLoadingCertificates = () => {
  const dispatch = useDispatch();
  const filter = useSelector(selectFilter);
  const {certificateList, status} = useSelector(selectCertificatesInfo);
  const {number, size, totalPages} = useSelector(selectCertificatesPage);

  const lastElement = useRef();
  const observer = useRef();

  useEffect(() => {
    dispatch(resetCertificates());
  }, [filter, dispatch]);

  useEffect(() => {
    if (observer.current) {
      observer.current.disconnect();
    }

    const callback = entries => {
      if (
        entries[0].isIntersecting &&
        certificateList.length > 0 &&
        number < totalPages - 1
      ) {
        dispatch(setPage(number + 1));
      }
    };

    observer.current = new IntersectionObserver(callback);
    observer.current.observe(lastElement.current);
  }, [number, totalPages, dispatch, certificateList.length]);

  useEffect(() => {
    if (status === 'idle') {
      dispatch(loadCertificates({number, size, filter}));
    }
  }, [number, size, filter, status, dispatch]);

  return [status, lastElement, certificateList];
};
