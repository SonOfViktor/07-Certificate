import {Paper, Table, TableBody, TableContainer} from '@mui/material';
import React, {useState} from 'react';
import {useSelector} from 'react-redux';
import AddEditCertificateModal from '../certificate-modal/AddEditCertificateModal';
import DeleteCertificateModal from '../certificate-modal/DeleteCertificateModal';
import CertificateTableHead from './CertificateTableHead';
import CertificateTablePagination from './CertificateTablePagination';
import CertificateTableRow from './CertificateTableRow';
import LoadingData from '../../ui/LoadingData';
import {useLoadingCertificates} from './hooks/useLoadingCertificates';
import SuccessSnackbar from '../../ui/SuccessSnackbar';
import LoadingAlert from '../../ui/LoadingAlert';
import {selectCertificatesInfo} from '../../../store/certificates/certificateSelectors';

const CertificateTable = () => {
  const {certificateList, status, error} = useSelector(selectCertificatesInfo);
  const [isModalOpen, setIsOpen] = useState(false);
  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);
  const [certificate, setCertificate] = useState();
  const {isSuccessMessageShow, changePage, changeSize} =
    useLoadingCertificates(status);

  const handleModalToggle = editCertificate => {
    setCertificate(editCertificate);
    setIsOpen(prev => !prev);
  };

  const handleDeleteModalToggle = deleteCertificate => {
    setCertificate(deleteCertificate);
    setIsDeleteModalOpen(prev => !prev);
  };

  return (
    <>
      <LoadingAlert status={status} error={error} />
      {status === 'loading' ? (
        <LoadingData />
      ) : (
        <TableContainer
          component={Paper}
          sx={{width: '98%', margin: '10px auto'}}>
          <Table sx={{minWidth: 650}}>
            <CertificateTableHead handleModalOpen={handleModalToggle} />
            <TableBody>
              {certificateList.map(row => (
                <CertificateTableRow
                  key={row.giftCertificateId}
                  certificate={row}
                  handleModalOpen={handleModalToggle}
                  handleDeleteModalOpen={handleDeleteModalToggle}
                />
              ))}
            </TableBody>
            <CertificateTablePagination
              changePage={changePage}
              changeSize={changeSize}
            />
            <AddEditCertificateModal
              certificate={certificate}
              open={isModalOpen}
              handleClose={handleModalToggle}
            />
            <DeleteCertificateModal
              certificate={certificate}
              open={isDeleteModalOpen}
              handleClose={handleDeleteModalToggle}
            />
          </Table>
        </TableContainer>
      )}
      <SuccessSnackbar
        isShow={isSuccessMessageShow}
        message={`Certificate was ${status} successfully`}
      />
    </>
  );
};

export default CertificateTable;
