import {styled} from '@mui/material';

const GridForm = styled('form')(({gap}) => ({
  display: 'grid',
  gridTemplateColumns: 'repeat(auto-fit, minmax(330px, 1fr))',
  gap: gap,
  padding: '20px 30px',
  backgroundColor: 'white',

  '& .buttonPlace': {
    gridColumn: '-1 / -2',
    marginTop: 30,
  },
  '@media screen and (max-width: 600px)': {
    padding: '20px 5px',
    'div .empty-cell': {
      display: 'none',
    },
  },
}));

export default GridForm;
