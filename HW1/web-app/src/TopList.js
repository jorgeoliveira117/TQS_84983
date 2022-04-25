import useFetch from "./useFetch";
import {Table} from 'react-bootstrap';
import TopListItem from "./TopListItem";


function TopList () {

    const topN = 10;
    const nContinents = 5;
    const URL = "http://localhost:8080/cases/top/" + (topN + nContinents);

    const { data , loading, error } = useFetch(URL);


    if(error)
        console.log(error);

    return ( 
        <div className="TopList">
            <h5>
                {/* topN */} 
                Countries with most cases today
            </h5>
            {loading && <h5>Loading...</h5>}
            {data &&
                <Table>
                    <thead>
                    <tr>
                        <th>Country</th>
                        <th>Cases</th>
                        <th>Deaths</th>
                    </tr>
                    </thead>
                    <tbody>
                    {
                        data.map((stats) => (
                            <TopListItem  key={stats.country} stats={stats} />
                        ))
                    }
                    </tbody>
                </Table>
            }
        </div>
    );
}
 
export default TopList;