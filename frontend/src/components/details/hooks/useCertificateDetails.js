import {useEffect} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {useNavigate, useParams} from 'react-router-dom';
import {selectCertificateDetails} from '../../../store/certificate-details/certificateDetailsSelectors';
import {
  clearDetails,
  loadCertificateById,
} from '../../../store/certificate-details/certificateDetailsSlice';

export const useCertificateDetails = () => {
  const navigator = useNavigate();
  const {id} = useParams();
  const dispatch = useDispatch();
  const {status, currentCertificate} = useSelector(selectCertificateDetails);

  useEffect(() => {
    if (status === 'rejected') {
      navigator('/not-found');
    }
  }, [status, navigator]);

  useEffect(() => {
    dispatch(loadCertificateById(id));

    return () => {
      dispatch(clearDetails());
    };
  }, [id, dispatch]);

  return [status, currentCertificate];
};
